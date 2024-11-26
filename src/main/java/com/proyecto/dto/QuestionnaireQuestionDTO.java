package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireQuestionDTO {
    /**
     * Identificador único de la pregunta.
     */
    private Long id;

    /**
     * Texto de la pregunta.
     */
    private String questionText;

    /**
     * Tipo de la pregunta.
     */
    private QuestionType questionType;

    /**
     * Opciones disponibles para la pregunta.
     */
    private List<QuestionOptionDTO> options;

    /**
     * Indica si la pregunta es obligatoria.
     */
    private boolean required;

    /**
     * Tipo de evento asociado a la pregunta.
     */
    private EventType eventType;
}