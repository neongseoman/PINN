package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameInitResponseDTO {
    private int gameId;
    private String roomName;
    private int themeId;
    private int leaderId;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    private LocalDateTime startedTime;
}
