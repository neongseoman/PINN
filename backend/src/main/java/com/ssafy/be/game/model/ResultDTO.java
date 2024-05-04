package com.ssafy.be.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultDTO {
    private int month;
    private TemperatureDTO temp;
    private PressureDTO pressure;
    private HumidityDTO humidity;
    private WindDTO wind;
    private PrecipitationDTO precipitation;
    private CloudDTO clouds;
    private double sunshineHours;
}
