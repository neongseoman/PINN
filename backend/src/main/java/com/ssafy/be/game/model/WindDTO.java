package com.ssafy.be.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WindDTO {
    private int min;
    private int max;
    private int median;
    private double mean;
    private int p25;
    private int p75;
    private double stDev;
    private int num;
}
