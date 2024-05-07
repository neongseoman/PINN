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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PinGuessVO extends SocketDTO {

    public PinGuessVO(LocalDateTime senderDateTime, String senderNickname, int senderGameId, int senderTeamId, int code, String msg) {
        super(senderDateTime, senderNickname, senderGameId, senderTeamId, code, msg);
    }
}
