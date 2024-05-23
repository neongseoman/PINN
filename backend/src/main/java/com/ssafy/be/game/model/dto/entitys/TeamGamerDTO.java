package com.ssafy.be.game.model.dto.entitys;

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
    private int colorId;
    private int gamerId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public TeamGamerDTO(TeamGamer teamGamer) {
        this.teamGamerId = teamGamer.getTeamGamerId();
        this.teamId = teamGamer.getTeamId();
        this.colorId = teamGamer.getColorId();
        this.gamerId = teamGamer.getGamerId();
        this.createdDate = teamGamer.getCreatedDate();
        this.updatedDate = teamGamer.getUpdatedDate();
    }

    public TeamGamer toEntity() {
        return TeamGamer.builder()
                .teamGamerId(this.teamGamerId)
                .teamId(this.teamId)
                .colorId(this.colorId)
                .gamerId(this.gamerId)
                .build();
    }
}
