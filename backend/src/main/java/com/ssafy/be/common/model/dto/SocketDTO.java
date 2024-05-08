package com.ssafy.be.common.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

//@AllArgsConstructor
@Getter
@Setter
@ToString
public class SocketDTO {
    private LocalDateTime senderDateTime;  // 송신자가 보낸 시간
    private String senderNickname;
    private int senderGameId;
    private int senderTeamId;
    private int code;
    private String msg;

    // nickname, gameId, teamId
    public SocketDTO() {
        this.senderDateTime = LocalDateTime.now();
    }

    // DTO 용도 사용 시 > 생성될 때 localdatetime.now()를 넣음
    public SocketDTO(String senderNickname, int senderGameId, int senderTeamId) {
        this.senderDateTime = LocalDateTime.now();
        this.senderNickname = senderNickname;
        this.senderGameId = senderGameId;
        this.senderTeamId = senderTeamId;
        this.code = 0;
        this.msg = null;
    }

    // VO 용도 사용 시 > LocalDateTime을 지정하여 param으로 입력해야 함
    public SocketDTO(LocalDateTime senderDateTime, String senderNickname, int senderGameId, int senderTeamId, int code, String msg) {
        this.senderDateTime = senderDateTime;
        this.senderNickname = senderNickname;
        this.senderGameId = senderGameId;
        this.senderTeamId = senderTeamId;
        this.code = code;
        this.msg = msg;
    }


    public void setCodeAndMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}