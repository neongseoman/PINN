package com.ssafy.be.common.component;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TeamGamerComponent {
    private int teamId;
    private String teamGamerColor;
    private int gamerId;
    private String nickname;
    private int teamGamerNumber;
}
