package com.ssafy.be.game.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamPinDTO {
    private int teamId;
    private int guessed;
    private double submitLat;
    private double submitLng;
}
