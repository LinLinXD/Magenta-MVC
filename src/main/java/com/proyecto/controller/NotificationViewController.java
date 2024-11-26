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

    /**
     * Añade atributos de notificación al modelo.
     *
     * @param model el modelo para la vista.
     * @param session la sesión HTTP actual.
     */
    @ModelAttribute
    public void addNotificationAttributes(Model model, HttpSession session) {
        String username = (String) session.getAttribute("USERNAME");
        if (username != null) {
            try {
                List<AppointmentNotificationDTO> notifications = notificationClient.getUnreadNotifications(username);
                if (notifications != null) {
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

    /**
     * Marca una notificación como leída.
     *
     * @param id el ID de la notificación.
     * @param request la solicitud HTTP actual.
     * @return la redirección a la página anterior.
     */
    @PostMapping("/{id}/read")
    public String markNotificationAsRead(@PathVariable Long id,
                                         HttpServletRequest request) {
            notificationClient.markNotificationAsRead(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }
}