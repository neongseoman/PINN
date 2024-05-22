package com.ssafy.be.game.model.dto;

import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoundFinishRequestDTO extends SocketDTO {
    private int roundNumber;

    public RoundFinishRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int roundNumber) {
        super(senderNickname, senderGameId, senderTeamId);
        setRoundNumber(roundNumber);
    }
}
