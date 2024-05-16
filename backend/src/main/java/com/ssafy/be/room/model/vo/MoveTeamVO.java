package com.ssafy.be.room.model.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MoveTeamVO {
    private int oldTeamId;
    private int newTeamId;
    private int newTeamNumber;

    private LocalDateTime senderDateTime;  // 송신자가 보낸 시간
    private String senderNickname;
    private int senderGameId;
    private int code;
    private String msg;
}
