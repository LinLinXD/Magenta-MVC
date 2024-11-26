package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentNotificationDTO {
    /**
     * Identificador único de la notificación.
     */
    private Long id;

    /**
     * Identificador de la cita asociada a la notificación.
     */
    private Long appointmentId;

    /**
     * Fecha y hora en que se envió la notificación.
     */
    private LocalDateTime notificationTime;

    /**
     * Indica si la notificación ha sido leída.
     */
    private boolean read;

    /**
     * Indica si la notificación ha sido enviada.
     */
    private boolean sent;

    /**
     * Mensaje de la notificación.
     */
    private String message;

    /**
     * Tipo de la notificación.
     */
    private NotificationType type;
}