package com.ssafy.be.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrecipitationDTO { // 강수량
    private double min;
    private double max;
    private double median;
    private double mean;
    private double p25;
    private double p75;
    private double stDev;
    private int num;
}
