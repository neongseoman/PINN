package com.ssafy.be.game.model.dto.hint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TemperatureDTO {
    private double recordMin;
    private double recordMax;
    private double averageMin;
    private double averageMax;
    private double median;
    private double mean;
    private double p25;
    private double p75;
    private double stDev;
    private int num;
}
