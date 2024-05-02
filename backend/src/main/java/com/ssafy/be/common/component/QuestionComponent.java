package com.ssafy.be.common.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class QuestionComponent {
    private int questionId; // db에서의 문제 id
    private int round; // 이 게임에서 이 문제가 출제될 라운드 번호
    private String questionName; // 문제 이름
    private double lat; // 위도
    private double lng; // 경도
    private List<HintComponent> hints; // 이 문제의 힌트
}
