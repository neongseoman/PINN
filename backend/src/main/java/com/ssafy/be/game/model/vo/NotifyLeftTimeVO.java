package com.ssafy.be.game.model.vo;

import com.ssafy.be.common.model.dto.ServerEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotifyLeftTimeVO {
    int leftTime;
    int stageTime;
    int stage; //0 before,1 stage 1,2 stage2,3 score , 4 wait
    int round;
    int code;
    String msg;

    public NotifyLeftTimeVO(int leftTime, int stageTime, int stage, int round, ServerEvent event) {
        this.leftTime = leftTime;
        this.stageTime = stageTime;
        this.stage = stage;
        this.round = round;
        this.code = event.getCode();
        this.msg = event.getMsg();
    }
}
