package com.ssafy.be.game.model.dto.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.game.model.domain.Team;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TeamDTO {
    private int teamId;
    private int gameId;
    private int colorId;
    private int teamNumber;
    private boolean isReady;
    private LocalDateTime lastReadyTime;
    private int finalRank;
    private int finalScore;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public TeamDTO(Team team) {
        this.teamId = team.getTeamId();
        this.gameId = team.getGameId();
        this.colorId = team.getColorId();
        this.teamNumber = team.getTeamNumber();
        this.isReady = team.isReady();
        this.lastReadyTime = team.getLastReadyTime();
        this.finalRank = team.getFinalRank();
        this.finalScore = team.getFinalScore();
        this.createdDate = team.getCreatedDate();
        this.updatedDate = team.getUpdatedDate();
    }

    public Team toEntity() {
        return Team.builder()
                .teamId(this.teamId)
                .gameId(this.gameId)
                .colorId(this.colorId)
                .teamNumber(this.teamNumber)
                .isReady(this.isReady)
                .lastReadyTime(this.lastReadyTime)
                .finalRank(this.finalRank)
                .finalScore(this.finalScore)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .build();
    }
}
