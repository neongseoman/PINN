package com.ssafy.be.common.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//@AllArgsConstructor
@Getter
@Setter
public class SocketDTO {
    private LocalDateTime senderDateTime;  // 송신자가 보낸 시간(Java 내 서버 시간)
    private String senderNickname;
    private int senderGameId;
    private int senderTeamId;
    private int code;
    private String msg;

    public SocketDTO(LocalDateTime senderDateTime, String senderNickname, int senderGameId, int senderTeamId, int code, String msg) {
        this.senderDateTime = senderDateTime;
        this.senderNickname = senderNickname;
        this.senderGameId = senderGameId;
        this.senderTeamId = senderTeamId;
        this.code = code;
        this.msg = msg;
    }

    public void setCodeAndMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}