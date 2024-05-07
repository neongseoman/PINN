package com.ssafy.be.game.model.dto.hint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WeatherAPIResponseDTO {
    private int code;
    private long cityId;
    private double calctime;
    private ResultDTO result;
}
