package com.ssafy.be.game.model.vo;

import com.ssafy.be.common.component.QuestionComponent;
import com.ssafy.be.common.component.TeamRoundComponent;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameResultVO {
    private int gameId;
    private int teamId;
    private List<List<TeamRoundComponent>> roundResults;
    private List<QuestionComponent> questions;
}
