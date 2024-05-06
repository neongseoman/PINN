package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.component.HintComponent;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoundInitResponseDTO extends SocketDTO {
    private int gameId;
    private int round;
    private int questionId; // db에서의 문제 id
    private String questionName; // 문제 이름
    private double lat; // 위도
    private double lng; // 경도
    private List<HintComponent> hints; // 이 문제의 힌트

    public RoundInitResponseDTO(LocalDateTime senderDateTime, String senderNickname, int senderGameId, int senderTeamId, int code, String msg) {
        super(senderDateTime, senderNickname, senderGameId, senderTeamId, code, msg);
    }
}