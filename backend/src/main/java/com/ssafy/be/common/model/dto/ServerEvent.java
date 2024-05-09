package com.ssafy.be.common.model.dto;

import lombok.Getter;

@Getter
public enum ServerEvent {
    START(1201,"Game Start. Waiting for players."),
    ROUND_START(1202,"Round Start. Stage 1 go"),
    STAGE_1_End(1203,"Server Send Hint, Stage 2 go"),
    STAGE_2_END(1204,"Stage 2 End. Go to Round Score Page"),
    ROUND_END(1205,"Round End."),
    GAME_SCORE(1206,"Go to Game Score Page"),
    GO_TO_ROOM(1207,"GO to Room");

    private final int code;
    private final String msg;
    ServerEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
