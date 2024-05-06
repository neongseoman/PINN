package com.ssafy.be.common.model.dto;

public class ServerSendEvent {
    int code;
    String msg;

    public ServerSendEvent(ServerEvent event){
        this.code = event.getCode();
        this.msg = event.getMsg();
    }
}
