package com.ssafy.be.gamer.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash(value = "token")
@AllArgsConstructor
@Builder
@ToString
public class GamerInfoVO {
    @Id
    private int id;
    private int gamerId;
    private String nickname;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

}
