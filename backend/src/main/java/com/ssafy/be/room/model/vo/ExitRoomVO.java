package com.ssafy.be.room.model.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExitRoomVO{
    private LocalDateTime senderDateTime;
    private String senderNickname;
    private int senderGameId;
    private int senderTeamId;
    private int senderTeamNumber;
    private int code;
    private String msg;
}
