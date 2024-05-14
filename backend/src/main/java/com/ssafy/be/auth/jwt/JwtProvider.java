package com.ssafy.be.auth.jwt;

import com.ssafy.be.auth.model.JwtPayload;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerDTO;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.gamer.repository.GamerLoginRedisRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Log4j2
public class JwtProvider {
    private SecretKey key;
    @Value("${jwt.kakao.access.expiration}")
    private long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${jwt.kakao.refresh.expiration}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    @Autowired
    private GamerLoginRedisRepository gamerLoginRedisRepository;

    public JwtProvider() {
        String secret = "b2b3ac692d4e4c68f608bfa880dbb6e5855c7e4ebadaf544b4417fe591066268";
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String[] generateAccessToken(GamerDTO gamerDTO) {
        JwtPayload jwtPayload = new JwtPayload(new Date(), ACCESS_TOKEN_EXPIRE_TIME, gamerDTO.getNickname(), gamerDTO.getGamerId());
        String accessToken = generateToken(jwtPayload);
        String refreshToken = generateRefreshToken(gamerDTO);
        return new String[]{accessToken, refreshToken};
    }

    public String generateRefreshToken(GamerDTO gamerDTO) {
        JwtPayload jwtPayload = new JwtPayload(new Date(), REFRESH_TOKEN_EXPIRE_TIME, gamerDTO.getNickname(), gamerDTO.getGamerId());
        String refreshToken = generateToken(jwtPayload);
        saveRefreshTokenToRedis(refreshToken, gamerDTO.getGamerId());
        return refreshToken;
    }

    public String generateToken(JwtPayload jwtPayload) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nickname", jwtPayload.getNickname());
        claims.put("gamerId", jwtPayload.getGamerId());

        return Jwts.builder()
                .issuer("moonjar")
                .subject("user identity") //발급 받는 사용자
                .claims(claims)
                .issuedAt(jwtPayload.getIssuedAt()) // 발급한 날짜
                .expiration(jwtPayload.getExpiresAt()) // 만료시간J.claims(claims)
                .signWith(key)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String accessToken) throws ExpiredJwtException {
        try{
            Claims claims = validateToken(accessToken);
            int gamerId = claims.get("gamerId", Integer.class);
            String nickname = claims.get("nickname", String.class);
            GamerPrincipalVO gamerPrincipalVO = new GamerPrincipalVO(gamerId,nickname);
            log.debug("{} 유저 인증 성공 " , nickname);
            return new UsernamePasswordAuthenticationToken(gamerPrincipalVO, "",List.of(new SimpleGrantedAuthority("USER")));
        } catch (ExpiredJwtException e){
            log.error("JWT Auth fail : {}", e.getMessage());
            throw e;
        }
    }

    public void saveRefreshTokenToRedis(String refreshToken,int gamerId) {
//        GamerInfoVO refreshTokenDTO = new GamerInfoVO(refreshToken,gamerId,REFRESH_TOKEN_EXPIRE_TIME);
//        gamerLoginRedisRepository.save(refreshTokenDTO);
    }

    public Claims validateToken(String accessToken) throws ExpiredJwtException {
        log.debug(accessToken);
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken);
            return claims.getPayload();
        } catch(ExpiredJwtException e){
            log.error(e.getMessage(), e.getHeader(), e.getClaims());
            throw new ExpiredJwtException(e.getHeader(),e.getClaims(),e.getMessage());
            // 만료 exception 처리
        }
    }

    public GamerPrincipalVO getGamerPrincipalVOByMessageHeader(StompHeaderAccessor accessor){
        String token = accessor.getNativeHeader("Auth").get(0);
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) authentication.getPrincipal();
        return gamerPrincipalVO;
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
