package com.ssafy.be.gamer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.security.Principal;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class GamerPrincipalVO implements Principal {
    int gamerId;
    String nickname;

    public GamerPrincipalVO(int gamerId, String nickname) {
        this.gamerId= gamerId;
        this.nickname = nickname;
    }

    @Override
    public String getName() {
        return nickname;
    }
}
