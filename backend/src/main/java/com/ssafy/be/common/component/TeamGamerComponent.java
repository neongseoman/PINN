package com.ssafy.be.common.component;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TeamGamerComponent {
    private int teamId;
    private int colorId;
    private int gamerId;
    private int teamGamerNumber;
}
