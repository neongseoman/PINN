package com.ssafy.be.gamer.controller;

import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import jakarta.servlet.ServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("gamer")
public class GamerController {

    @GetMapping("userInfo")
    public BaseResponse getUserInfo(ServletRequest request){

        return new BaseResponse(BaseResponseStatus.SUCCESS,);
    }
}
