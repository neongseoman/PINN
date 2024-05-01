package com.ssafy.be.auth.model;

import lombok.Getter;

import java.util.Calendar;
import java.util.Date;

@Getter
public class JwtPayload {
    Date issuedAt;
    Date expiresAt;
    String nickname;
    int gamerId;

    public JwtPayload(Date dateTime, long expireTime, String nickname, int gamerId) {
        this.issuedAt = dateTime;
        this.expiresAt = new Date(issuedAt.getTime() + expireTime);
        this.nickname = nickname;
        this.gamerId = gamerId;
    }
}
