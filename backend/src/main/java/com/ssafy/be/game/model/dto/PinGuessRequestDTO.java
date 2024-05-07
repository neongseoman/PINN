package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PinGuessRequestDTO extends SocketDTO {


    public PinGuessRequestDTO(String senderNickname, int senderGameId, int senderTeamId) {
        // 추가한 변수들 넣는거 잊지 말기!
        super(senderNickname, senderGameId, senderTeamId);
    }
}
