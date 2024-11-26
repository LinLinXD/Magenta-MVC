package com.proyecto.dto;

/**
 * Enum que representa el estado de una cita.
 */
public enum AppointmentStatus {
    /**
     * La cita está pendiente de confirmación.
     */
    PENDING,

    /**
     * La cita ha sido confirmada.
     */
    CONFIRMED,

    /**
     * La cita ha sido cancelada.
     */
    CANCELLED,

    /**
     * La cita ha sido completada.
     */
    COMPLETED
}