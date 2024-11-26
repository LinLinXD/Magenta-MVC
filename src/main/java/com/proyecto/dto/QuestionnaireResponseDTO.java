package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireResponseDTO {
    /**
     * Identificador único de la pregunta.
     */
    private Long questionId;

    /**
     * Respuesta a la pregunta.
     */
    private String response;

    /**
     * Pregunta asociada a la respuesta.
     */
    private QuestionnaireQuestionDTO question;
}