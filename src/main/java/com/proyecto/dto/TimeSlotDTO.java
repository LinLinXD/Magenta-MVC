package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean available;
    private String unavailableReason;

    // Metodo de conveniencia para obtener la fecha
    public LocalDate getDate() {
        return startTime.toLocalDate();
    }

    // Metodo de conveniencia para obtener el día del mes
    public int getDayOfMonth() {
        return startTime.getDayOfMonth();
    }

    // Metodo para verificar si es hoy
    public boolean isToday() {
        return getDate().equals(LocalDate.now());
    }

    // Metodo para verificar si es un día pasado
    public boolean isPast() {
        return getDate().isBefore(LocalDate.now());
    }
}