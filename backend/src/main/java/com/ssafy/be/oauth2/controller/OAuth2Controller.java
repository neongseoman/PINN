package com.ssafy.be.oauth2.controller;


import com.ssafy.be.gamer.repository.GamerRepository;
import com.ssafy.be.oauth2.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/code/kakao")
    public void getAuthCode(HttpServletResponse res, @RequestParam("code") String code) throws IOException {
        String kakaoAccessToken = oAuth2Service.getAccessToken();

        res.addHeader("access-token", kakaoAccessToken);
        res.sendRedirect("http://www.pinn.kr:3000");
    }

}
