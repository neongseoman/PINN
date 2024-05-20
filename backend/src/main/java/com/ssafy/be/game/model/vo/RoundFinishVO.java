package com.ssafy.be.game.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.component.TeamRoundComponent;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoundFinishVO extends SocketDTO {
    // gameId: socketDTO의 senderGameId
    List<TeamRoundComponent> teamRoundResults;

    public RoundFinishVO(String senderNickname, int senderGameId, int senderTeamId, List<TeamRoundComponent> teamRoundResults) {
        super(senderNickname, senderGameId, senderTeamId);
        setTeamRoundResults(teamRoundResults);
    }
}
