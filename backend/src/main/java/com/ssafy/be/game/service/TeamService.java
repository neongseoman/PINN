package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.game.model.dto.entitys.TeamDTO;

import java.util.List;

public interface TeamService {

    int createTeam(TeamDTO teamDTO) throws BaseException;

    int updateTeam(TeamDTO teamDTO) throws BaseException;

    int deleteTeam(int teamId) throws BaseException;

    TeamDTO getTeam(int teamId) throws BaseException;

    List<TeamDTO> getAllTeam() throws BaseException;

    List<TeamDTO> getAllTeamByGameId(int gameId) throws BaseException;

}
