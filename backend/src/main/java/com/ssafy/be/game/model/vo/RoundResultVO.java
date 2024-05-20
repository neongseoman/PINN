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
public class RoundResultVO {
    private int gameId;
    private int roundNumber;
    private QuestionComponent question; // 해당 라운드의 정답
    private List<TeamRoundComponent> roundResult;
}
