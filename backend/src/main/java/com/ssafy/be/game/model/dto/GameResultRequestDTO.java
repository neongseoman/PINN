package com.ssafy.be.game.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameResultRequestDTO {
    private int gameId;
    private int teamId;
}
