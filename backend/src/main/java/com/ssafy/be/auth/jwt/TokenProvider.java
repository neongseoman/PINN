package com.ssafy.be.auth.jwt;

import com.ssafy.be.auth.model.JwtType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Component
public class TokenProvider {
    @Value("${jwt.kakao.secret_key}")
    private String secretKey;
//    private final TokenService tokenService;
    @Value("${jwt.kakao.access.expiration}")
    private static long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${jwt.kakao.refresh.expiration}")
    private static long REFRESH_TOKEN_EXPIRE_TIME;
    private static final String KEY_ROLE = "role";

    public String[] generateAccessToken(Authentication authentication) {
        String accessToken = generateToken(authentication,ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = generateRefreshToken(authentication,REFRESH_TOKEN_EXPIRE_TIME,accessToken);
        return new String[]{accessToken,refreshToken};
    }
    public String generateRefreshToken(Authentication authentication,long  expiresTime,String accessToken) {
        String refreshToken =  generateToken(authentication,expiresTime);
//        tokenService.saveOrUpdate(authentication.getName(),accessToken,refreshToken);
        return refreshToken;
    }
    private String generateToken(Authentication authentication,long expiresTime){


        return "tokenprovider";
    }
}
