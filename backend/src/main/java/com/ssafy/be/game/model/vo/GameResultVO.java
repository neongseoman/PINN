package com.ssafy.be.game.model.vo;

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

    // 1. 라운드별 결과
    private List<List<TeamRoundComponent>> roundResults;

    // 2. 게임 전체 결과
    //

    // 3. 사용자가 속한 팀의 해당 게임 통계
    //
}
