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
    /**
     * Identificador único de la cita.
     */
    private Long id;

    /**
     * Nombre del usuario que agendó la cita.
     */
    private String name;

    /**
     * Correo electrónico del usuario que agendó la cita.
     */
    private String email;

    /**
     * Nombre de usuario del usuario que agendó la cita.
     */
    private String username;

    /**
     * Fecha y hora de la cita.
     */
    private LocalDateTime appointmentDateTime;

    /**
     * Estado de la cita.
     */
    private AppointmentStatus status;

    /**
     * Tipo de evento de la cita.
     */
    private EventType eventType;

    /**
     * Respuestas al cuestionario asociadas a la cita.
     */
    private List<QuestionnaireResponseDTO> responses;

    /**
     * Fecha y hora de creación de la cita.
     */
    private LocalDateTime createdAt;

    /**
     * Mensaje de error asociado a la cita, si existe.
     */
    private String error;
}