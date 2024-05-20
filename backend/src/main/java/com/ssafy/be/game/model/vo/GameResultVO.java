package com.ssafy.be.game.model.vo;

import com.ssafy.be.common.component.QuestionComponent;
import com.ssafy.be.common.component.TeamRoundComponent;
import com.ssafy.be.game.model.dto.entity.QuestionDTO;
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

    // 1. 라운드별 결과
    private List<List<TeamRoundComponent>> roundResults;

    // 2. 라운드별 문제
    private List<QuestionComponent> questions;

//    private GameResultComponent gameResult;
}
