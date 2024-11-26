package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionDTO {
    /**
     * Identificador único de la opción.
     */
    private Long id;

    /**
     * Texto de la opción.
     */
    private String optionText;
}