package com.ssafy.be.oauth2.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerDTO;
import com.ssafy.be.oauth2.dto.KakaoOAuthConfig;
import com.ssafy.be.oauth2.service.OAuth2Service;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final JwtProvider jwtProvider;
    private final KakaoOAuthConfig kakaoOAuthConfig;
    @Value("${jwt.kakao.access.expiration}")
    private long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${jwt.kakao.refresh.expiration}")
    private long REFRESH_TOKEN_EXPIRE_TIME;
//    http://www.pinn.kr/api/oauth/code/kakao/local
    @GetMapping("/code/kakao/local")
    public BaseResponse getAuthCodeToLocal(HttpServletRequest req, HttpServletResponse res, @RequestParam("code") String code) throws IOException {
//        for (Cookie cookie : req.getCookies()) {
//            log.info(cookie.getName() + " : " + cookie.getValue());
//        }
        Map<String, String> token = new HashMap();
        log.info("getAuthCode : " + code);
        String kakaoAccessToken = oAuth2Service.getAccessTokenFromlocal(code);
        GamerDTO gamer = oAuth2Service.getUserInfo(kakaoAccessToken);

        String[] tokens = jwtProvider.generateAccessToken(gamer);

        ResponseCookie acookie = ResponseCookie.from("access_token", tokens[0])
                .maxAge((int) ACCESS_TOKEN_EXPIRE_TIME)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None").build();

        ResponseCookie rcookie = ResponseCookie.from("refresh_token", tokens[1])
                .maxAge((int) REFRESH_TOKEN_EXPIRE_TIME)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None").build();
        token.put("accessToken", tokens[0]); // 테스트

        res.setHeader("Set-Cookie", rcookie.toString());
        res.addHeader("auth",acookie.toString());
        res.sendRedirect("http://localhost:3000/lobby?code=" + acookie.toString());

        return new BaseResponse(BaseResponseStatus.SUCCESS, token); // 테스트
    }

    @GetMapping("/code/kakao/server")
    public BaseResponse getAuthCodeToServer(HttpServletRequest req, HttpServletResponse res, @RequestParam("code") String code) throws IOException {
//        for (Cookie cookie : req.getCookies()) {
//            log.info(cookie.getName() + " : " + cookie.getValue());
//        }
        Map<String, String> token = new HashMap();
        log.info("getAuthCode : " + code);
        String kakaoAccessToken = oAuth2Service.getAccessTokenFromServer(code);
        GamerDTO gamer = oAuth2Service.getUserInfo(kakaoAccessToken);

        String[] tokens = jwtProvider.generateAccessToken(gamer);

        ResponseCookie acookie = ResponseCookie.from("access_token", tokens[0])
                .maxAge((int) ACCESS_TOKEN_EXPIRE_TIME)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None").build();

        ResponseCookie rcookie = ResponseCookie.from("refresh_token", tokens[1])
                .maxAge((int) REFRESH_TOKEN_EXPIRE_TIME)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None").build();
        token.put("accessToken", tokens[0]); // 테스트

        res.setHeader("Set-Cookie", rcookie.toString());
        res.addHeader("auth",acookie.toString());
        res.sendRedirect("http://pinn.kr/lobby?code=" + acookie.toString());

        return new BaseResponse(BaseResponseStatus.SUCCESS, token); // 테스트
    }

    @GetMapping("/code/kakao/test")
    public BaseResponse getAuthCodeToTest(HttpServletRequest req, HttpServletResponse res, @RequestParam("code") String code) throws IOException {
//        for (Cookie cookie : req.getCookies()) {
//            log.info(cookie.getName() + " : " + cookie.getValue());
//        }
        Map<String, String> token = new HashMap();
        log.info("getAuthCode : " + code);
        String kakaoAccessToken = oAuth2Service.getAccessTokenFromTest(code,kakaoOAuthConfig.redirect_uri());
        GamerDTO gamer = oAuth2Service.getUserInfo(kakaoAccessToken);

        String[] tokens = jwtProvider.generateAccessToken(gamer);

        ResponseCookie acookie = ResponseCookie.from("access_token", tokens[0])
                .maxAge((int) ACCESS_TOKEN_EXPIRE_TIME)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None").build();

        ResponseCookie rcookie = ResponseCookie.from("refresh_token", tokens[1])
                .maxAge((int) REFRESH_TOKEN_EXPIRE_TIME)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None").build();
        token.put("accessToken", tokens[0]); // 테스트

        res.setHeader("Set-Cookie", rcookie.toString());
        res.addHeader("auth",acookie.toString());
        res.sendRedirect("http://localhost:3000/lobby?code=" + acookie.toString());

        return new BaseResponse(BaseResponseStatus.SUCCESS, token); // 테스트
    }

    @GetMapping("test")
    public void test(){
        log.info("Filter test clear");
    }
    @GetMapping("getAccessToken")
    public void getToken(ServletRequest req, HttpServletResponse res) throws IOException {

    }

}
