package com.ssafy.be.gamer.model.dto;

import lombok.*;
import com.ssafy.be.gamer.model.domain.GamerLog;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GamerLogDTO {
    private Long gamerLogId;
    private int gamerId;
    private int gameId;
    private int teamId;
    private int rank;
    private String teamColor;
    private int isRoomLeader;
    private int isTeamLeader;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public GamerLogDTO(GamerLog gamerLog) {
        this.gamerLogId = gamerLog.getGamerLogId();
        this.gamerId = gamerLog.getGamerId();
        this.gameId = gamerLog.getGameId();
        this.teamId = gamerLog.getTeamId();
        this.rank = gamerLog.getRank();
        this.teamColor = gamerLog.getTeamColor();
        this.isRoomLeader = gamerLog.getIsRoomLeader();
        this.isTeamLeader = gamerLog.getIsTeamLeader();
        this.createdDate = gamerLog.getCreatedDate();
        this.updatedDate = gamerLog.getUpdatedDate();
    }

    public GamerLog toEntity() {
        return GamerLog.builder()
                .gamerLogId(this.gamerLogId)
                .gamerId(this.gamerId)
                .gameId(this.gameId)
                .teamId(this.teamId)
                .rank(this.rank)
                .teamColor(this.teamColor)
                .isRoomLeader(this.isRoomLeader)
                .isTeamLeader(this.isTeamLeader)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .build();
    }
}
