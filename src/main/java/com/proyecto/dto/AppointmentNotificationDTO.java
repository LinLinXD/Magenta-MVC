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
    private Long id;
    private Long appointmentId;
    private LocalDateTime notificationTime;
    private boolean read;
    private String message;
    private NotificationType type;

    // Método de utilidad para formatear mensajes
    public String getFormattedMessage() {
        if (appointmentId == null) {
            return message;
        }
        return message + " - ID: " + appointmentId;
    }
}