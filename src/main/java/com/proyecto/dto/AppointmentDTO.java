package com.proyecto.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;
    private String name;  
    private String email;
    private String username;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private EventType eventType;
    private List<QuestionnaireResponseDTO> responses;
    private LocalDateTime createdAt;
    private String error;
}