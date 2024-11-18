package com.proyecto.controller;

import com.proyecto.client.NotificationClient;
import com.proyecto.dto.AppointmentNotificationDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationViewController {

    private final NotificationClient notificationClient;

    @ModelAttribute
    public void addNotificationAttributes(Model model, HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");
        if (username != null) {
            try {
                List<AppointmentNotificationDTO> notifications = notificationClient.getUnreadNotifications(username);
                if (notifications != null) {
                    // Contar solo las notificaciones enviadas y no leídas
                    long unreadCount = notifications.stream()
                            .filter(n -> n.isSent() && !n.isRead())
                            .count();

                    model.addAttribute("notifications", notifications);
                    model.addAttribute("unreadNotificationsCount", unreadCount);
                } else {
                    model.addAttribute("notifications", new ArrayList<>());
                    model.addAttribute("unreadNotificationsCount", 0);
                }
            } catch (Exception e) {
                model.addAttribute("notifications", new ArrayList<>());
                model.addAttribute("unreadNotificationsCount", 0);
            }
        }
    }

    @PostMapping("/{id}/read")
    public String markNotificationAsRead(@PathVariable Long id,
                                         HttpServletRequest request) {
        try {
            notificationClient.markNotificationAsRead(id);
        } catch (Exception e) {
            // Manejar error si es necesario
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }
}