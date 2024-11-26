package com.proyecto.dto;

/**
 * Enum que representa los diferentes tipos de notificaciones.
 */
public enum NotificationType {
    /**
     * Notificación cuando se crea una cita.
     */
    APPOINTMENT_CREATED,

    /**
     * Notificación cuando se cancela una cita.
     */
    APPOINTMENT_CANCELLED,

    /**
     * Notificación cinco días antes de una cita.
     */
    FIVE_DAYS_BEFORE,

    /**
     * Notificación dos días antes de una cita.
     */
    TWO_DAYS_BEFORE,

    /**
     * Notificación un día antes de una cita.
     */
    ONE_DAY_BEFORE,

    /**
     * Notificación una hora antes de una cita.
     */
    ONE_HOUR_BEFORE
}