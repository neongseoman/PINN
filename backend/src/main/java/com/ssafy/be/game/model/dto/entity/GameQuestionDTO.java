package com.ssafy.be.game.model.dto.entity;

import com.ssafy.be.game.model.domain.GameQuestion;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameQuestionDTO {
    private int gameQuestionId;
    private int gameId;
    private int questionId;
    private int roundNumber;
    private LocalDateTime createdDate;

    @Builder
    public GameQuestionDTO(GameQuestion gameQuestion) {
        this.gameQuestionId = gameQuestion.getGameQuestionId();
        this.gameId = gameQuestion.getGameId();
        this.questionId = gameQuestion.getQuestionId();
        this.roundNumber = gameQuestion.getRoundNumber();
        this.createdDate = gameQuestion.getCreatedDate();
    }

    public GameQuestion toEntity() {
        return GameQuestion.builder()
                .gameQuestionId(this.gameQuestionId)
                .gameId(this.gameId)
                .questionId(this.questionId)
                .roundNumber(this.roundNumber)
                .build();
    }
}
