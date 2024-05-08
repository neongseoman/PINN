package com.ssafy.be.common.component;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class TeamRoundComponent {
    private int roundNumber; // 라운드 번호
    private int submitStage; // 최종 핀 찍은 스테이지 (1 or 2)
    private LocalDateTime submitTime; // guess 한 시간
    private double submitLat; // 가장 최근(or guess한) 핀 찍은 위도
    private double submitLng; // 가장 최근(or guess한) 핀 찍은 경도
    private int roundScore; // submitLat, submitLng 바탕으로 계산된 점수
    private boolean guessed; // 아직 변경 중인지, 최종 guess 완료했는지 체크
}
