package com.ssafy.be.gamer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class GamerPrincipalVO {
    int gamerId;
    String nickname;

    public GamerPrincipalVO(int gamerId, String nickname) {
        this.gamerId= gamerId;
        this.nickname = nickname;
    }
}
