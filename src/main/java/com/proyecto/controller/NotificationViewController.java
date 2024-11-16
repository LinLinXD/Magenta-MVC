package com.proyecto.controller;

import com.proyecto.client.NotificationClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationViewController {

    private final NotificationClient notificationClient;

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