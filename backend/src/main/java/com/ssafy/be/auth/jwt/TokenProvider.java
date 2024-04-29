package com.ssafy.be.auth.jwt;

import com.ssafy.be.gamer.model.GamerDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

//@RequiredArgsConstructor
@Component
@Log4j2
public class TokenProvider {
    @Value("${jwt.kakao.secret_key}")
    private String secretKey;
    private Key key;
    @Value("${jwt.kakao.access.expiration}")
    private long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${jwt.kakao.refresh.expiration}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    @PostConstruct
    public void init(){
        key = Jwts.SIG.HS256.key().build();
        System.out.println("key a: " + key.getAlgorithm());
        System.out.println("key f: " + key.getFormat());
    }

    public String[] generateAccessToken(GamerDTO gamerDTO) {
        String accessToken = generateToken(ACCESS_TOKEN_EXPIRE_TIME,gamerDTO);
        String refreshToken = generateRefreshToken(REFRESH_TOKEN_EXPIRE_TIME,gamerDTO);
        return new String[]{accessToken,refreshToken};
    }
    public String generateRefreshToken(long  expiresTime,GamerDTO gamerDTO) {
        String refreshToken =  generateToken(expiresTime,gamerDTO);
//        tokenService.saveOrUpdate(authentication.getName(),accessToken,refreshToken);
        return refreshToken;
    }

    private String generateToken(long expiresTime, GamerDTO gamerDTO){
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime ex = LocalDateTime.now().plusSeconds(expiresTime);
        Date expiration = Date.from(ex.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .issuer("moonjar310") // 발급한 사람
                .subject(gamerDTO.getNickname()) //발급 받는 사용자
                .issuedAt(now) // 발급한 날짜
                .expiration(expiration) // 만료시간
                .claim("nickname",gamerDTO.getNickname()) // 게이머 닉네임
                .claim("gamerId",gamerDTO.getGamerId()) // 게이머 ID
                .signWith(key)
                .compact();
    }
}
