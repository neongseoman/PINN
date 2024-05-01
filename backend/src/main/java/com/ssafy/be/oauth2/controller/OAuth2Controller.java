package com.ssafy.be.oauth2.controller;

import com.ssafy.be.auth.jwt.TokenProvider;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerDTO;
import com.ssafy.be.oauth2.service.OAuth2Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final TokenProvider tokenProvider;

    @GetMapping("/code/kakao")
    public BaseResponse getAuthCode(HttpServletResponse res, @RequestParam("code") String code) throws IOException {
        log.info("getAuthCode : " + code);
        String kakaoAccessToken = oAuth2Service.getAccessToken(code);
        GamerDTO gamer = oAuth2Service.getUserInfo(kakaoAccessToken);

        String[] tokens = tokenProvider.generateAccessToken(gamer);
        Cookie acookie = new Cookie("access_token", tokens[0]);
        acookie.setSecure(true);
        acookie.setHttpOnly(true);
        acookie.setPath("/");
        Cookie rcookie = new Cookie("refresh_token", tokens[1]);
        rcookie.setSecure(true);
        rcookie.setHttpOnly(true);
        acookie.setPath("/");
        res.setHeader("Set-Cookie",tokens[0]);
        res.setHeader("Set-Cookie",tokens[1]);
        res.sendRedirect("http://www.pinn.kr/lobby");
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }


}
