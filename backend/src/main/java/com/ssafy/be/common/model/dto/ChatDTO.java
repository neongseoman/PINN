package com.ssafy.be.common.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatDTO extends SocketDTO {
    private String content;

    public ChatDTO(LocalDateTime senderDateTime, String senderNickname, int senderGameId, int senderTeamId, int code, String msg, String content) {
        super(senderDateTime, senderNickname, senderGameId, senderTeamId, code, msg);
        this.content = content;
    }
}