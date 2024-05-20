package com.ssafy.be.common.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerSendEvent {
    int code;
    String msg;
    int round;

    public ServerSendEvent(ServerEvent event, int round){
        this.code = event.getCode();
        this.msg = event.getMsg();
        this.round = round;
    }
}
