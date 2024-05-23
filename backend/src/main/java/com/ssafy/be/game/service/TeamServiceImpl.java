package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.game.model.domain.Team;
import com.ssafy.be.game.model.dto.entitys.TeamDTO;
import com.ssafy.be.game.model.repository.TeamRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Autowired
    private TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /////////////

    @Override
    public int createTeam(TeamDTO teamDTO) throws BaseException {
        try {
            Team team = teamDTO.toEntity();
            return teamRepository.save(team).getTeamId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public int updateTeam(TeamDTO teamDTO) throws BaseException {
        try {
            Team existTeam = teamRepository.findById(teamDTO.getTeamId()).orElse(null);
            if (existTeam == null) throw new BaseException(BaseResponseStatus.OOPS);

            existTeam.setGameId(existTeam.getGameId());
            existTeam.setColorId(existTeam.getColorId());
            existTeam.setTeamNumber(existTeam.getTeamNumber());
            existTeam.setReady(existTeam.isReady());
            existTeam.setLastReadyTime(existTeam.getLastReadyTime());
            existTeam.setFinalRank(existTeam.getFinalRank());
            existTeam.setFinalScore(existTeam.getFinalScore());

            return teamRepository.save(existTeam).getTeamId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public int deleteTeam(int teamId) throws BaseException {
        try {
            Team existTeam = teamRepository.findById(teamId).orElse(null);
            if (existTeam == null) throw new BaseException(BaseResponseStatus.OOPS);

            teamRepository.deleteById(teamId);
            return teamId;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public TeamDTO getTeam(int teamId) throws BaseException {
        try {
            Team existTeam = teamRepository.findById(teamId).orElse(null);
            if (existTeam == null) {
                throw new BaseException(BaseResponseStatus.OOPS);
            } else {
                return new TeamDTO(existTeam);
            }
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<TeamDTO> getAllTeam() throws BaseException {
        try {
            List<Team> teamList = teamRepository.findAll();
            List<TeamDTO> teamDTOList = new ArrayList<>();
            for (Team team : teamList) {
                teamDTOList.add(new TeamDTO(team));
            }
            return teamDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<TeamDTO> getAllTeamByGameId(int gameId) throws BaseException {
        try {
            List<Team> teamList = teamRepository.findAllByGameId(gameId);
            List<TeamDTO> teamDTOList = new ArrayList<>();
            for (Team team : teamList) {
                teamDTOList.add(new TeamDTO(team));
            }
            return teamDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }
}
