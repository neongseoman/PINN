package com.ssafy.be.game.model.dto;
import com.ssafy.be.game.model.domain.Question;
import lombok.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QuestionDTO {
    private int questionId;
    private int themeId;
    private String questionName;
    private double lat;
    private double lng;
    private int used;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public QuestionDTO(Question question) {
        this.questionId = question.getQuestionId();
        this.themeId = question.getThemeId();
        this.questionName = question.getQuestionName();
        this.lat = question.getLat();
        this.lng = question.getLng();
        this.used = question.getUsed();
        this.createdDate = question.getCreatedDate();
        this.updatedDate = question.getUpdatedDate();
    }

    public Question toEntity() {
        return Question.builder()
                .questionId(this.questionId)
                .themeId(this.themeId)
                .questionName(this.questionName)
                .lat(this.lat)
                .lng(this.lng)
                .used(this.used)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .build();
    }
}
