package com.ssafy.be.game.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoundFinishVO extends SocketDTO {
    // TODO: 보내줄 "라운드 결과" 형식 정의 필요

    RoundFinishVO(String senderNickname, int senderGameId, int senderTeamId/**/) {
        super(senderNickname, senderGameId, senderTeamId);
        //
    }
}
