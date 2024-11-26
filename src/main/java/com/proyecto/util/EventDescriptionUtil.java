package com.proyecto.util;

import com.proyecto.dto.EventType;

public class EventDescriptionUtil {

    /**
     * Obtiene la descripción del evento en español.
     *
     * @param type el tipo de evento.
     * @return la descripción del evento en español.
     */
    public static String getEventDescriptionEs(EventType type) {
        return switch (type) {
            case WEDDING -> "Celebra tu amor con una boda inolvidable";
            case SWEET_FIFTEEN -> "Haz realidad el sueño de los quince años";
            case COMMUNION -> "Prepara una comunión especial y significativa";
            case BAPTISM -> "Celebra el bautizo con una ceremonia única";
            case CORPORATE -> "Organiza eventos empresariales profesionales";
            case OTHER -> "Personaliza cualquier tipo de evento especial";
        };
    }

    /**
     * Obtiene la descripción del evento en inglés.
     *
     * @param type el tipo de evento.
     * @return la descripción del evento en inglés.
     */
    public static String getEventDescriptionEn(EventType type) {
        return switch (type) {
            case WEDDING -> "Celebrate your love with an unforgettable wedding";
            case SWEET_FIFTEEN -> "Make the dream of fifteen years come true";
            case COMMUNION -> "Prepare a special and meaningful communion";
            case BAPTISM -> "Celebrate the baptism with a unique ceremony";
            case CORPORATE -> "Organize professional corporate events";
            case OTHER -> "Customize any type of special event";
        };
    }

    /**
     * Obtiene la descripción del evento en portugués.
     *
     * @param type el tipo de evento.
     * @return la descripción del evento en portugués.
     */
    public static String getEventDescriptionBr(EventType type) {
        return switch (type) {
            case WEDDING -> "Celebre seu amor com um casamento inesquecível";
            case SWEET_FIFTEEN -> "Realize o sonho dos quinze anos";
            case COMMUNION -> "Prepare uma comunhão especial e significativa";
            case BAPTISM -> "Celebre o batismo com uma cerimônia única";
            case CORPORATE -> "Organize eventos corporativos profissionais";
            case OTHER -> "Personalize qualquer tipo de evento especial";
        };
    }
}