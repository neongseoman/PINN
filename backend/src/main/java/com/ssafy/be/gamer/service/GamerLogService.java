package com.ssafy.be.gamer.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.gamer.model.dto.GamerLogDTO;

import java.util.List;

public interface GamerLogService {

    Long createGamerLog(GamerLogDTO gamerLogDTO) throws BaseException;

    Long updateGamerLog(GamerLogDTO gamerLogDTO) throws BaseException;

    Long deleteGamerLog(Long gamerLogId) throws BaseException;

    GamerLogDTO getGamerLog(Long gamerLogId) throws BaseException;

    List<GamerLogDTO> getAllGamerLogByGamerId(int gamerId) throws BaseException;

}
