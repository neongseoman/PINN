package com.ssafy.be.room.model.dto;

import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;

@Getter
public class MoveTeamDTO extends SocketDTO {

    private int oldTeamId;
    private int newTeamId;

    public MoveTeamDTO(String senderNickname, int senderGameId, int senderTeamId, int oldTeamId, int newTeamId) {
        super(senderNickname, senderGameId, senderTeamId);
        this.oldTeamId = oldTeamId;
        this.newTeamId = newTeamId;
    }
}
