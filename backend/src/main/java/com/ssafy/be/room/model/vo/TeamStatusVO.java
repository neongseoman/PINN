package com.ssafy.be.room.model.vo;

import com.ssafy.be.common.model.dto.SocketDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamStatusVO{
    private boolean teamStatus;
    private LocalDateTime senderDateTime;  // 송신자가 보낸 시간
    private String senderNickname;
    private int senderGameId;
    private int senderTeamId;
    private int code;
    private String msg;
}
