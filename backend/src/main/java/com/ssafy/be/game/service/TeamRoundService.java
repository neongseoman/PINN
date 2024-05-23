package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.game.model.dto.entitys.TeamRoundDTO;

import java.util.List;

public interface TeamRoundService {

    Long createTeamRound(TeamRoundDTO teamRoundDTO) throws BaseException;

    Long updateTeamRound(TeamRoundDTO teamRoundDTO) throws BaseException;

    Long deleteTeamRound(Long teamRoundId) throws BaseException;

    TeamRoundDTO getTeamRound(Long teamRoundId) throws BaseException;

    List<TeamRoundDTO> getAllTeamRound() throws BaseException;

    List<TeamRoundDTO> getAllTeamRoundByTeamId(int teamId) throws BaseException;
    
}
