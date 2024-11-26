package com.proyecto.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentDTO {
    @NotNull(message = "La fecha y hora de la cita son obligatorias")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "El tipo de evento es obligatorio")
    private EventType eventType;

    @Builder.Default
    private List<QuestionnaireResponseDTO> responses = new ArrayList<>();

    @Override
    public String toString() {
        return "CreateAppointmentDTO{" +
                "appointmentDateTime=" + appointmentDateTime +
                ", eventType=" + eventType +
                ", responses=" + responses +
                '}';
    }
}