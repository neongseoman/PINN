package com.ssafy.be.gamer.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.gamer.model.dto.GamerStatusDTO;

import java.util.List;

public interface GamerStatusService {

    int createGamerStatus(GamerStatusDTO gamerStatusDTO) throws BaseException;

    int updateGamerStatus(GamerStatusDTO gamerStatusDTO) throws BaseException;

    int deleteGamerStatus(int gamerId) throws BaseException;

    GamerStatusDTO getGamerStatusByGamerId(int gamerId) throws BaseException;

    List<GamerStatusDTO> getAllGamerStatus() throws BaseException;

    GamerStatusDTO updateWinAndPlayCount(int gamerId) throws BaseException; // wincount++ & playcount++

    GamerStatusDTO updatePlayCount(int gamerId) throws BaseException; // playcount++
}
