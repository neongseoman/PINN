package com.ssafy.be.common.model.dto;

import lombok.Getter;

@Getter
public enum ServerEvent {
    START(1,"Game Start"),
    ROUND_START(2,"Round Start"),
    HINT(3,"Server Send Hint"),
    ROUND_SCORE(4,"Go to Round Score Page"),
    GAME_SCORE(5,"Go to Game Score Page");

    private final int code;
    private final String msg;
    ServerEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
