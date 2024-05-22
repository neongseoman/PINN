package com.ssafy.be.game.model.dto.entity;

import com.ssafy.be.game.model.domain.TeamRound;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamRoundDTO {
    private Long teamRoundId;
    private int teamId;
    private int roundNumber;
    private int roundScore;
    private int submitStage;
    private LocalDateTime submitTime;
    private double submitLat;
    private double submitLng;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public TeamRoundDTO(TeamRound teamRound) {
        this.teamRoundId = teamRound.getTeamRoundId();
        this.teamId = teamRound.getTeamId();
        this.roundNumber = teamRound.getRoundNumber();
        this.roundScore = teamRound.getRoundScore();
        this.submitStage = teamRound.getSubmitStage();
        this.submitTime = teamRound.getSubmitTime();
        this.submitLat = teamRound.getSubmitLat();
        this.submitLng = teamRound.getSubmitLng();
        this.createdDate = teamRound.getCreatedDate();
        this.updatedDate = teamRound.getUpdatedDate();
    }

    public TeamRound toEntity() {
        return TeamRound.builder()
                .teamRoundId(this.teamRoundId)
                .teamId(this.teamId)
                .roundNumber(this.roundNumber)
                .roundScore(this.roundScore)
                .submitStage(this.submitStage)
                .submitTime(this.submitTime)
                .submitLat(this.submitLat)
                .submitLng(this.submitLng)
                .build();
    }
}
