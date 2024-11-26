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
    /**
     * Hora de inicio del intervalo de tiempo.
     */
    private LocalDateTime startTime;

    /**
     * Hora de finalización del intervalo de tiempo.
     */
    private LocalDateTime endTime;

    /**
     * Indica si el intervalo de tiempo está disponible.
     */
    private boolean available;

    /**
     * Razón por la cual el intervalo de tiempo no está disponible.
     */
    private String unavailableReason;

    /**
     * Metodo de conveniencia para obtener la fecha.
     *
     * @return la fecha del intervalo de tiempo.
     */
    public LocalDate getDate() {
        return startTime.toLocalDate();
    }

    /**
     * Metodo de conveniencia para obtener el día del mes.
     *
     * @return el día del mes del intervalo de tiempo.
     */
    public int getDayOfMonth() {
        return startTime.getDayOfMonth();
    }

    /**
     * Metodo para verificar si el intervalo de tiempo es hoy.
     *
     * @return true si el intervalo de tiempo es hoy, false en caso contrario.
     */
    public boolean isToday() {
        return getDate().equals(LocalDate.now());
    }

    /**
     * Metodo para verificar si el intervalo de tiempo es un día pasado.
     *
     * @return true si el intervalo de tiempo es un día pasado, false en caso contrario.
     */
    public boolean isPast() {
        return getDate().isBefore(LocalDate.now());
    }
}