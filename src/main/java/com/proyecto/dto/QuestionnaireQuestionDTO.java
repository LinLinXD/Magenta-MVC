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
    private Long id;
    private String questionText;
    private QuestionType questionType;
    private List<QuestionOptionDTO> options;
    private boolean required;
    private EventType eventType;
}