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
public class RoundFinishRequestDTO extends SocketDTO {
    // gameId는 socketDTO의 senderGameId를 사용함
    private int roundNumber;

    RoundFinishRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int roundNumber) {
        super(senderNickname, senderGameId, senderTeamId);
        setRoundNumber(roundNumber);
    }
}
