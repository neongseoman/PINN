package com.ssafy.be.gamer.controller;

import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.gamer.model.dto.NicknameRequestDTO;
import com.ssafy.be.gamer.service.GamerService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Server;
import org.springframework.web.bind.annotation.*;

import java.time.chrono.IsoEra;
@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/gamer")
public class GamerController {
    private final GamerService gamerService;


    @GetMapping("/userInfo")
    public BaseResponse getGamerInfo(ServletRequest req){
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");

        return new BaseResponse(BaseResponseStatus.SUCCESS,gamerPrincipalVO);
    }

    @PostMapping("/nickname")
    public BaseResponse updateGamerNickname(ServletRequest req, @RequestBody NicknameRequestDTO nicknameRequestDTO){
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        int gamerId = gamerPrincipalVO.getGamerId();
        gamerService.updateNickname(gamerId,nicknameRequestDTO);

        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

}
