package com.ssafy.be.game.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PinMoveVO extends SocketDTO {
    // TODO: 핀 찍은 사용자 gamerId 필요한지 재검토 필요
    private int gamerId;
    private double submitLat;
    private double submitLng;
    private double roundNumber;
    private double submitStage;

    public PinMoveVO(LocalDateTime senderDateTime, String senderNickname, int senderGameId, int senderTeamId, int code, String msg) {
        super(senderDateTime, senderNickname, senderGameId, senderTeamId, code, msg);
    }
}
