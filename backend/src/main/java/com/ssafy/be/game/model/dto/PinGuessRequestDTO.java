package com.ssafy.be.game.model.dto;

import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PinGuessRequestDTO extends SocketDTO {
    private int roundNumber; // 현재 라운드
    private int guessStage; // 현재 스테이지

    public PinGuessRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int roundNumber, int guessStage) {
        super(senderNickname, senderGameId, senderTeamId);
        setRoundNumber(roundNumber);
        setGuessStage(guessStage);
    }
}
