package com.ssafy.be.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherAPIResponseDTO {
    private int cod;
    private long cityId;
    private double calctime;
    private ResultDTO result;

}
