package com.ssafy.be.common.component;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamGamerComponent {
    private Long teamGamerId;
    private int teamId;
    private int colorId;
    private int gamerId;

}
