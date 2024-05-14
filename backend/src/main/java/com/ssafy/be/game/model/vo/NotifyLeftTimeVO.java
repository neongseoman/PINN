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
    String stage;
    int code;
    String msg;

    public NotifyLeftTimeVO(int leftTime, int stageTime, String stage, ServerEvent serverEvent) {
        this.leftTime = leftTime;
        this.stageTime = stageTime;
        this.stage = stage;
        this.code = serverEvent.getCode();
        this.msg = serverEvent.getMsg();
    }
}
