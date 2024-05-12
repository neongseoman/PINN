package com.ssafy.be.game.model.dto;

import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CursorDTO extends SocketDTO {
    private double lat; // 입력을 몰라서 일단 lat, lng로 해놓음; 필요 시 수정.
    private double lng;

    public CursorDTO(String senderNickname, int senderGameId, int senderTeamId, double lat, double lng) {
        super(senderNickname, senderGameId, senderTeamId);
        setLat(lat);
        setLng(lng);
    }
}
