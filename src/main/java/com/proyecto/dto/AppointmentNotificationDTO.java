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
    private boolean sent;
    private String message;
    private NotificationType type;
}