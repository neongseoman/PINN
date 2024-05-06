package com.ssafy.be.game.model.dto;

import com.ssafy.be.game.model.domain.GameAndQuestion;
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
public class GameAndQuestionDTO {
    private int gameQuestionId;
    private int gameId;
    private int questionId;
    private int roundNumber;
    private LocalDateTime createdDate;

    @Builder
    public GameAndQuestionDTO(GameAndQuestion gameAndQuestion) {
        this.gameQuestionId = gameAndQuestion.getGameQuestionId();
        this.gameId = gameAndQuestion.getGameId();
        this.questionId = gameAndQuestion.getQuestionId();
        this.roundNumber = gameAndQuestion.getRoundNumber();
        this.createdDate = gameAndQuestion.getCreatedDate();
    }

    public GameAndQuestion toEntity() {
        return GameAndQuestion.builder()
                .gameQuestionId(this.gameQuestionId)
                .gameId(this.gameId)
                .questionId(this.questionId)
                .roundNumber(this.roundNumber)
                .createdDate(this.createdDate)
                .build();
    }
}
