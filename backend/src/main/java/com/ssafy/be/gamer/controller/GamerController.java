package com.ssafy.be.gamer.controller;

import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.chrono.IsoEra;

@RestController
@Log4j2
@RequestMapping("/gamer")
public class GamerController {

    @GetMapping("/userInfo")
    public BaseResponse getGamerInfo(ServletRequest req){
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");

        return new BaseResponse(BaseResponseStatus.SUCCESS,gamerPrincipalVO);
    }

    @PostMapping("/nickname")
    public BaseResponse editGamerName(ServletRequest req){

    }

    @PostMapping("/image")
    public BaseResponse editImage(ServletRequest req){
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }
}
