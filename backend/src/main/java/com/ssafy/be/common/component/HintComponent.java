package com.ssafy.be.common.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class HintComponent {
    private int hintId; // db에 저장된 힌트 id ?
    private int hintTypeId; // 힌트 타입 id
    private String hintTypeName; // 힌트 타입 이름 = 힌트 이름
    private String hintValue; // 힌트 값
    private int offerStage; // 이 힌트가 제공될 스테이지 번호(1/2)
}
