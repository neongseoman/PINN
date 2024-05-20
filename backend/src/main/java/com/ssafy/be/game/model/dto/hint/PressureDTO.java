package com.ssafy.be.game.model.dto.hint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PressureDTO {
    private int min;
    private int max;
    private int median;
    private double mean;
    private int p25;
    private int p75;
    private double stDev;
    private int num;
}
