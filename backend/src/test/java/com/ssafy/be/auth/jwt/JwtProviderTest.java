package com.ssafy.be.auth.jwt;

import com.ssafy.be.auth.model.JwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@DisplayName("JWT Access Token 발급")
@Nested
class JwtProviderTest {
    private final long exprie_time = 10000000;
    private final String secret = "b2b3ac692d4e4c68f608bfa880dbb6e5855c7e4ebadaf544b4417fe591066268";
    private SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    private final JwtProvider jwtProvider = new JwtProvider();

    @DisplayName("토큰 발급 성공")
    @Test
    void _token_generate_success(){
        //given
        Date issueDate = new Date(System.currentTimeMillis());
        JwtPayload jwtPayload = new JwtPayload(issueDate,exprie_time,"testname",1234);

        String accessToken = jwtProvider.generateToken(jwtPayload);

        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();

//        Assertions.assertThat()

        Assertions.assertEquals(claims.getIssuer(),"moonjar");
        Assertions.assertEquals(claims.getSubject(),"user identity");
//        Assertions.assertEquals(claims.getIssuedAt(),issueDate);
//        Assertions.assertEquals(claims.getExpiration(),jwtPayload.getExpiresAt());
        Assertions.assertEquals(claims.get("nickname"),"testname");
        Assertions.assertEquals(claims.get("gamerId"),1234);
    }
//    private Date roundOffMillis(Date date) { issueAt
//        return new Date(date.getTime() / 1000 * 1000);
//    }
    @DisplayName("검증 성공")
    @Test
    void _token_validate_success(){
        Date issueDate = new Date(System.currentTimeMillis());
        JwtPayload jwtPayload = new JwtPayload(issueDate,exprie_time,"testname",1234);

        String accessToken = jwtProvider.generateToken(jwtPayload);

        Claims claims = jwtProvider.validateToken(accessToken);
        Assertions.assertEquals(claims.get("nickname"),"testname");
        Assertions.assertEquals(claims.get("gamerId"),1234);
    }
    @DisplayName("만료되면 실패")
    @Test
    void _token_validate_expire_fail(){
        Date issueDate = new Date(System.currentTimeMillis());
        JwtPayload jwtPayload = new JwtPayload(issueDate,-10000,"testname",1234);

        String accessToken = jwtProvider.generateToken(jwtPayload);
//        Claims claims = jwtProvider.validateToken(accessToken);
        Assertions.assertThrows(ExpiredJwtException.class,()->{
            jwtProvider.validateToken(accessToken);
        });
    }
}