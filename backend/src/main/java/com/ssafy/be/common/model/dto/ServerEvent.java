package com.ssafy.be.common.model.dto;

import lombok.Getter;

@Getter
public enum ServerEvent {
    START(1201,"Game Start. Waiting for 5."),
    ROUND_START(1202,"Round Start. Stage 1 go"),
    STAGE_1_END(1203,"Server Send Hint, Stage 2 go"),
    STAGE_2_END(1204,"Stage 2 End. Go to Round Score Page"),
//    SCORE_PAGE(1205,"Go to Round Score Page."),
    ROUND_END(1206,"Round End. Wait Next Round"),
    GAME_SCORE(1207,"Go to Game Score Page"),
    GO_TO_ROOM(1208,"GO to Room");

    private final int code;
    private final String msg;
    ServerEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
