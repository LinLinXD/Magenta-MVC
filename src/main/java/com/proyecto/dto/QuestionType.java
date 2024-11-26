package com.proyecto.dto;

/**
 * Enum que representa los diferentes tipos de preguntas en un cuestionario.
 */
public enum QuestionType {
    /**
     * Pregunta de opción múltiple.
     */
    MULTIPLE_CHOICE,

    /**
     * Pregunta de opción única.
     */
    SINGLE_CHOICE,

    /**
     * Pregunta de texto libre.
     */
    TEXT,

    /**
     * Pregunta de tipo fecha.
     */
    DATE,

    /**
     * Pregunta de tipo numérico.
     */
    NUMBER
}