package com.ssafy.be.game.model.dto;

import com.ssafy.be.game.model.domain.Hint;
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
public class HintDTO {
    private int hintId;
    private int questionId;
    private int hintTypeId;
    private String hintValue;
    private int offerStage;
    private boolean useOrNot;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public HintDTO(Hint hint) {
        this.hintId = hint.getHintId();
        this.questionId = hint.getQuestionId();
        this.hintTypeId = hint.getHintTypeId();
        this.hintValue = hint.getHintValue();
        this.offerStage = hint.getOfferStage();
        this.useOrNot = hint.isUseOrNot();
        this.createdDate = hint.getCreatedDate();
        this.updatedDate = hint.getUpdatedDate();
    }

    public Hint toEntity() {
        return Hint.builder()
                .hintId(this.hintId)
                .questionId(this.questionId)
                .hintTypeId(this.hintTypeId)
                .hintValue(this.hintValue)
                .offerStage(this.offerStage)
                .useOrNot(this.useOrNot)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .build();
    }
}
