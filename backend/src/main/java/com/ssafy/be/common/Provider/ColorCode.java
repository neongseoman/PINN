package com.ssafy.be.common.Provider;

import lombok.Getter;

@Getter
public enum ColorCode {

    // 팀 색상
    // 빨강, 진빨강, 주황, 연두, 하늘, 보라, 진핑, 연핑, 연빨강, 회색
    RED(1, "rgba(255, 0, 61, 1)"),
    BURGUNDY(2, "rgba(182, 53, 53, 1)"),
    ORANGE(3, "rgba(255, 111, 0, 1)"),
    LIGHT_GREEN(4, "rgba(153, 155, 41, 1)"),
    SKY_BLUE(5, "rgba(0, 131, 143, 1)"),
    PURPLE(6, "rgba(105, 53, 170, 1)"),
    HOT_PINK(7, "rgba(251, 52, 159, 1)"),
    LIGHT_PINK(8, "rgba(255, 172, 207, 1)"),
    LIGHT_RED(9, "rgba(188, 157, 157, 1)"),
    GRAY(10, "rgba(85, 85, 85, 1)"),

    // 멤버 색상
    //
    NEON_GREEN(11, "rgba(0, 255, 194, 1)"),
    NEON_PINK(12, "rgba(255, 38, 116, 1)"),
    NEON_YELLOW(13, "rgba(242, 228, 35, 1)");


    private final int teamNumber;
    private final String colorCode;

    ColorCode(int teamNumber, String colorCode) {
        this.teamNumber = teamNumber;
        this.colorCode = colorCode;
    }
}
