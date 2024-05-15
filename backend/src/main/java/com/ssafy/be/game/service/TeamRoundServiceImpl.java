package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.game.model.domain.TeamGamer;
import com.ssafy.be.game.model.domain.TeamRound;
import com.ssafy.be.game.model.dto.entity.TeamRoundDTO;
import com.ssafy.be.game.model.repository.TeamRoundRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TeamRoundServiceImpl implements TeamRoundService {

    private final TeamRoundRepository teamRoundRepository;

    @Autowired
    private TeamRoundServiceImpl(TeamRoundRepository teamRoundRepository) {
        this.teamRoundRepository = teamRoundRepository;
    }

    /////////

    @Override
    public Long createTeamRound(TeamRoundDTO teamRoundDTO) throws BaseException {
        try {
            TeamRound teamRound = teamRoundDTO.toEntity();
            return teamRoundRepository.save(teamRound).getTeamRoundId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public Long updateTeamRound(TeamRoundDTO teamRoundDTO) throws BaseException {
        try {
            TeamRound existTeamRound = teamRoundRepository.findById(teamRoundDTO.getTeamRoundId()).orElse(null);
            if (existTeamRound == null) throw new BaseException(BaseResponseStatus.OOPS);

            existTeamRound.setTeamId(teamRoundDTO.getTeamId());
            existTeamRound.setRoundNumber(teamRoundDTO.getRoundNumber());
            existTeamRound.setRoundScore(teamRoundDTO.getRoundScore());
            existTeamRound.setSubmitStage(teamRoundDTO.getSubmitStage());
            existTeamRound.setSubmitTime(teamRoundDTO.getSubmitTime());
            existTeamRound.setSubmitLat(teamRoundDTO.getSubmitLat());
            existTeamRound.setSubmitLng(teamRoundDTO.getSubmitLng());

            return teamRoundRepository.save(existTeamRound).getTeamRoundId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public Long deleteTeamRound(Long teamRoundId) throws BaseException {
        try {
            TeamRound existTeamRound = teamRoundRepository.findById(teamRoundId).orElse(null);
            if (existTeamRound == null) throw new BaseException(BaseResponseStatus.OOPS);

            teamRoundRepository.deleteById(teamRoundId);
            return teamRoundId;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public TeamRoundDTO getTeamRound(Long teamRoundId) throws BaseException {
        try {
            TeamRound existTeamRound = teamRoundRepository.findById(teamRoundId).orElse(null);
            if (existTeamRound == null) {
                throw new BaseException(BaseResponseStatus.OOPS);
            } else {
                return new TeamRoundDTO(existTeamRound);
            }
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<TeamRoundDTO> getAllTeamRound() throws BaseException {
        try {
            List<TeamRound> teamRoundList = teamRoundRepository.findAll();
            List<TeamRoundDTO> teamRoundDTOList = new ArrayList<>();
            for (TeamRound teamRound : teamRoundList) {
                teamRoundDTOList.add(new TeamRoundDTO(teamRound));
            }
            return teamRoundDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<TeamRoundDTO> getAllTeamRoundByTeamId(int teamId) throws BaseException {
        try {
            List<TeamRound> teamRoundList = teamRoundRepository.findAllByTeamId(teamId);
            List<TeamRoundDTO> teamRoundDTOList = new ArrayList<>();
            for (TeamRound teamRound : teamRoundList) {
                teamRoundDTOList.add(new TeamRoundDTO(teamRound));
            }
            return teamRoundDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }
}
