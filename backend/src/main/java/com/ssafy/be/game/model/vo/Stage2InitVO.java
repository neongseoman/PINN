package com.ssafy.be.game.model.vo;

import com.ssafy.be.common.component.HintComponent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Stage2InitVO {
    private int gameId;
    private int round;
    private int stage;
    private List<HintComponent> hints;
}