package com.ssafy.be.common.component;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class TeamRoundComponent implements Comparable<TeamRoundComponent> {
    private int teamId;
    private String colorCode; // 팀 색상

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
    public int compareTo(@NotNull TeamRoundComponent o) { // 정렬 기준: 현 라운드 점수 내림차순
        return o.roundScore - this.roundScore;
    }
}
