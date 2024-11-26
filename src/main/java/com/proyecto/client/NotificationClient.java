package com.proyecto.client;

import com.proyecto.configuration.FeignConfig;
import com.proyecto.dto.AppointmentNotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Cliente Feign para el servicio de notificaciones.
 */
@FeignClient(
        name = "notification-service",
        url = "${auth.service.url:localhost:8080}",
        configuration = FeignConfig.class
)
public interface NotificationClient {

    /**
     * Obtiene las notificaciones no leídas de un usuario.
     *
     * @param username el nombre de usuario del usuario.
     * @return una lista de notificaciones de citas no leídas.
     */
    @GetMapping("/notifications/unread/{username}")
    List<AppointmentNotificationDTO> getUnreadNotifications(@PathVariable String username);

    /**
     * Obtiene todas las notificaciones de un usuario.
     *
     * @param username el nombre de usuario del usuario.
     * @return una lista de todas las notificaciones de citas.
     */
    @GetMapping("/appointments/user/{username}/notifications")
    List<AppointmentNotificationDTO> getAllNotifications(@PathVariable String username);

    /**
     * Marca una notificación como leída.
     *
     * @param notificationId el ID de la notificación.
     */
    @PutMapping("/notifications/{notificationId}/read")
    void markNotificationAsRead(@PathVariable Long notificationId);

    /**
     * Obtiene el conteo de notificaciones no leídas de un usuario.
     *
     * @param username el nombre de usuario del usuario.
     * @return el número de notificaciones no leídas.
     */
    @GetMapping("/notifications/count/{username}")
    long getUnreadNotificationCount(@PathVariable String username);
}