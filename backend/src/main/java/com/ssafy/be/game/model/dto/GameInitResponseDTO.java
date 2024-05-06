package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameInitResponseDTO extends SocketDTO {
    private int gameId;
    private String roomName;
    private int themeId;
    private int leaderId;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    private LocalDateTime startedTime;


    public GameInitResponseDTO(LocalDateTime senderDateTime, String senderNickname, int senderGameId, int senderTeamId, int code, String msg) {
        super(senderDateTime, senderNickname, senderGameId, senderTeamId, code, msg);
    }
}