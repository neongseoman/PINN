package com.ssafy.be.game.model.vo;

import com.ssafy.be.common.component.TeamRoundComponent;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RoundFinishVO extends SocketDTO {
    List<TeamRoundComponent> teamRoundResults;

    public RoundFinishVO(String senderNickname, int senderGameId, int senderTeamId, List<TeamRoundComponent> teamRoundResults) {
        super(senderNickname, senderGameId, senderTeamId);
        setTeamRoundResults(teamRoundResults);
    }
}
