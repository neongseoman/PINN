package com.ssafy.be.lobby.model.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class EnterRoomVO {
    private LocalDateTime senderDateTime;
    private String senderNickname;
    private int senderGameId;
    private int senderTeamId;
    private int senderTeamNumber;
    private int code;
    private String msg;
}
