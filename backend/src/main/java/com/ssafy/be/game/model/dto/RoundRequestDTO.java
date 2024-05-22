package com.ssafy.be.game.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoundRequestDTO {
    private int gameId;
    private int round;
}