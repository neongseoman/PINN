package com.ssafy.be.game.model.vo;

import com.ssafy.be.common.component.TeamRoundComponent;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoundResultVO {
    private int gameId;
    private int roundNumber;
    private List<TeamRoundComponent> roundResult;
}
