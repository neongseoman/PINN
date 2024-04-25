package com.ssafy.be.auth.controller;

import com.ssafy.be.auth.service.AuthService;
import com.ssafy.be.common.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssafy.be.common.response.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("api/v1/oauth")
public class AuthController {
    @Autowired
    AuthService authService;

    @GetMapping("kakao")
    public BaseResponse<?> getKakaoToken(){

        return new BaseResponse<>(SUCCESS);
    }

    @GetMapping("test")
    public BaseResponse<?> test(){
        return new BaseResponse<>(SUCCESS);
    }
}
