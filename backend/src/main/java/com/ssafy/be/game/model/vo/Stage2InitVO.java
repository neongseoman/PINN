package com.ssafy.be.game.model.vo;

import com.ssafy.be.common.component.HintComponent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Stage2InitVO /*extends SocketDTO*/ {
    private int gameId;
    private int round;
    private int stage;
    private List<HintComponent> hints;

//    public Stage2InitVO(LocalDateTime senderDateTime, String senderNickname, int senderGameId, int senderTeamId, int code, String msg) {
//        super(senderDateTime, senderNickname, senderGameId, senderTeamId, code, msg);
//    }
}