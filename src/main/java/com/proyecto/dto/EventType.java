package com.proyecto.dto;

/**
 * Enum que representa los diferentes tipos de eventos.
 */
public enum EventType {
    /**
     * Evento de boda.
     */
    WEDDING("Boda"),

    /**
     * Evento de quinceañera.
     */
    SWEET_FIFTEEN("Quinceañera"),

    /**
     * Evento de comunión.
     */
    COMMUNION("Comunión"),

    /**
     * Evento de bautizo.
     */
    BAPTISM("Bautizo"),

    /**
     * Evento corporativo.
     */
    CORPORATE("Evento Corporativo"),

    /**
     * Otro tipo de evento.
     */
    OTHER("Otro");

    /**
     * Nombre para mostrar del tipo de evento.
     */
    private final String displayName;

    /**
     * Constructor para inicializar el nombre para mostrar del tipo de evento.
     *
     * @param displayName el nombre para mostrar del tipo de evento.
     */
    EventType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Obtiene el nombre para mostrar del tipo de evento.
     *
     * @return el nombre para mostrar del tipo de evento.
     */
    public String getDisplayName() {
        return displayName;
    }
}