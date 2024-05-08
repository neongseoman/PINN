package com.ssafy.be.game.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TeamRoundResultVO implements Comparable<TeamRoundResultVO> { // 각 팀의 각 라운드 결과 정보
    private int roundNumber; // 라운드 번호

    private int roundRank; // 현 라운드 점수 기준 등수
    private int totalRank; // 현 라운드까지의 총점 기준 등수

    private int roundScore; // 현 라운드 점수 - submitLat, submitLng 바탕으로 계산
    private int totalScore; // 현 라운드까지의 총점

    private boolean guessed; // 현 라운드에서의 guess 여부
    private int submitStage; // guess 한 스테이지 (1 or 2)
    private LocalDateTime submitTime; // 마지막 핀 찍은 시간 or Guess 한 시간
    private double submitLat; // 가장 최근(or guess한) 핀 찍은 위도
    private double submitLng; // 가장 최근(or guess한) 핀 찍은 경도

    @Override
    public int compareTo(@NotNull TeamRoundResultVO o) {
        return 0;
    }
}
