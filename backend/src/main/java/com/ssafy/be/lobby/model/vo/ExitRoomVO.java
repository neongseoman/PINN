package com.ssafy.be.lobby.model.vo;

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

    private boolean changeLeader;  // 리더 변경 여부
    private String newLeaderNickname;  // 변경된 리더 닉네임
    private int newLeaderTeamId; // 변경된 리더 팀번호
    private int newLeaderTeamNumber; // 변경된 리더 팀 내 위치

    private int code;
    private String msg;
}
