package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.game.model.dto.entitys.TeamGamerDTO;

import java.util.List;

public interface TeamGamerService {

    Long createTeamGamer(TeamGamerDTO teamGamerDTO) throws BaseException;

    Long updateTeamGamer(TeamGamerDTO teamGamerDTO) throws BaseException;

    Long deleteTeamGamer(Long teamGamerId) throws BaseException;

    TeamGamerDTO getTeamGamer(Long teamGamerId) throws BaseException;

    List<TeamGamerDTO> getAllTeamGamer() throws BaseException;

    List<TeamGamerDTO> getAllTeamGamerByTeamId(int teamId) throws BaseException;

    List<TeamGamerDTO> getAllTeamGamerByGamerId(int gamerId) throws BaseException;

    List<TeamGamerDTO> getAllTeamGamerByColorId(int colorId) throws BaseException;
}
