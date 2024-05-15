package com.ssafy.be.game.model.dto.entity;

import com.ssafy.be.game.model.domain.TeamGamer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamGamerDTO {
    private Long teamGamerId;
    private int teamId;
    private String colorCode;
    private int gamerId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public TeamGamerDTO(TeamGamer teamGamer) {
        this.teamGamerId = teamGamer.getTeamGamerId();
        this.teamId = teamGamer.getTeamId();
        this.colorCode = teamGamer.getColorCode();
        this.gamerId = teamGamer.getGamerId();
        this.createdDate = teamGamer.getCreatedDate();
        this.updatedDate = teamGamer.getUpdatedDate();
    }

    public TeamGamer toEntity() {
        return TeamGamer.builder()
                .teamGamerId(this.teamGamerId)
                .teamId(this.teamId)
                .colorCode(this.colorCode)
                .gamerId(this.gamerId)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .build();
    }
}
