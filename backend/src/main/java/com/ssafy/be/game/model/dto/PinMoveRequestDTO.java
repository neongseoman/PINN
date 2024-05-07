package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PinMoveRequestDTO extends SocketDTO {
    // gameId, teamId : socketDTO에서 가져다 사용
    private double submitLat; // 핀 찍은 위치의 위도
    private double submitLng; // 핀 찍은 위치의 경도
    private int roundNumber;
    private int submitStage;

    public PinMoveRequestDTO(String senderNickname, int senderGameId, int senderTeamId, double submitLat, double submitLng, int roundNumber, int submitStage) {
        super(senderNickname, senderGameId, senderTeamId);
        this.submitLat = submitLat;
        this.submitLng = submitLng;
        this.roundNumber = roundNumber;
        this.submitStage = submitStage;
    }
}
