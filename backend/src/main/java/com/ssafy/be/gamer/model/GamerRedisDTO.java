package com.ssafy.be.gamer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("gamerDTO")
@NoArgsConstructor
@Entity
public class GamerRedisDTO {

    @Id
    @GeneratedValue
    private Long id;
    private String refreshToken;
    private int gamerId;

    public GamerRedisDTO(String refreshToken, int gamerId) {
        this.refreshToken = refreshToken;
        this.gamerId = gamerId;
    }
}
