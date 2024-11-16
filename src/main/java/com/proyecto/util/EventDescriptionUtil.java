package com.proyecto.util;


import com.proyecto.dto.EventType;

public class EventDescriptionUtil {
    public static String getEventDescription(EventType type) {
        return switch (type) {
            case WEDDING -> "Celebra tu amor con una boda inolvidable";
            case SWEET_FIFTEEN -> "Haz realidad el sueño de los quince años";
            case COMMUNION -> "Prepara una comunión especial y significativa";
            case BAPTISM -> "Celebra el bautizo con una ceremonia única";
            case CORPORATE -> "Organiza eventos empresariales profesionales";
            case OTHER -> "Personaliza cualquier tipo de evento especial";
        };
    }
}