package com.proyecto.client;

import com.proyecto.configuration.FeignConfig;
import com.proyecto.dto.AppointmentNotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(
        name = "notification-service",
        url = "${auth.service.url:localhost:8080}",
        configuration = FeignConfig.class
)
public interface NotificationClient {

    @GetMapping("/notifications/unread/{username}")
    List<AppointmentNotificationDTO> getUnreadNotifications(@PathVariable String username);

    @GetMapping("/appointments/user/{username}/notifications")
    List<AppointmentNotificationDTO> getAllNotifications(@PathVariable String username);

    @PutMapping("/notifications/{notificationId}/read")
    void markNotificationAsRead(@PathVariable Long notificationId);

    @GetMapping("/notifications/count/{username}")
    long getUnreadNotificationCount(@PathVariable String username);
}