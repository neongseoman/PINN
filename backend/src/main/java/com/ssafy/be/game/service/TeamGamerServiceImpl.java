package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.game.model.domain.TeamGamer;
import com.ssafy.be.game.model.dto.entitys.TeamGamerDTO;
import com.ssafy.be.game.model.repository.TeamGamerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TeamGamerServiceImpl implements TeamGamerService {

    private final TeamGamerRepository teamGamerRepository;

    @Autowired
    private TeamGamerServiceImpl(TeamGamerRepository teamGamerRepository) {
        this.teamGamerRepository = teamGamerRepository;
    }

    ///////////////

    @Override
    public Long createTeamGamer(TeamGamerDTO teamGamerDTO) throws BaseException {
        try {
            TeamGamer teamGamer = teamGamerDTO.toEntity();
            return teamGamerRepository.save(teamGamer).getTeamGamerId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public Long updateTeamGamer(TeamGamerDTO teamGamerDTO) throws BaseException {
        try {
            TeamGamer existTeamGamer = teamGamerRepository.findById(teamGamerDTO.getTeamGamerId()).orElse(null);
            if (existTeamGamer == null) throw new BaseException(BaseResponseStatus.OOPS);

            existTeamGamer.setTeamId(teamGamerDTO.getTeamId());
            existTeamGamer.setGamerId(teamGamerDTO.getGamerId());
            existTeamGamer.setColorId(teamGamerDTO.getColorId());

            return teamGamerRepository.save(existTeamGamer).getTeamGamerId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public Long deleteTeamGamer(Long teamGamerId) throws BaseException {
        try {
            TeamGamer existTeamGamer = teamGamerRepository.findById(teamGamerId).orElse(null);
            if (existTeamGamer == null) throw new BaseException(BaseResponseStatus.OOPS);

            teamGamerRepository.deleteById(teamGamerId);
            return teamGamerId;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public TeamGamerDTO getTeamGamer(Long teamGamerId) throws BaseException {
        try {
            TeamGamer existTeamGamer = teamGamerRepository.findById(teamGamerId).orElse(null);
            if (existTeamGamer == null) {
                throw new BaseException(BaseResponseStatus.OOPS);
            } else {
                return new TeamGamerDTO(existTeamGamer);
            }
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<TeamGamerDTO> getAllTeamGamer() throws BaseException {
        try {
            List<TeamGamer> teamGamerList = teamGamerRepository.findAll();
            List<TeamGamerDTO> teamGamerDTOList = new ArrayList<>();
            for (TeamGamer teamGamer : teamGamerList) {
                teamGamerDTOList.add(new TeamGamerDTO(teamGamer));
            }
            return teamGamerDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<TeamGamerDTO> getAllTeamGamerByTeamId(int teamId) throws BaseException {
        try {
            List<TeamGamer> teamGamerList = teamGamerRepository.findAllByTeamId(teamId);
            List<TeamGamerDTO> teamGamerDTOList = new ArrayList<>();
            for (TeamGamer teamGamer : teamGamerList) {
                teamGamerDTOList.add(new TeamGamerDTO(teamGamer));
            }
            return teamGamerDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<TeamGamerDTO> getAllTeamGamerByGamerId(int gamerId) throws BaseException {
        try {
            List<TeamGamer> teamGamerList = teamGamerRepository.findAllByGamerId(gamerId);
            List<TeamGamerDTO> teamGamerDTOList = new ArrayList<>();
            for (TeamGamer teamGamer : teamGamerList) {
                teamGamerDTOList.add(new TeamGamerDTO(teamGamer));
            }
            return teamGamerDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<TeamGamerDTO> getAllTeamGamerByColorId(int colorId) throws BaseException {
        try {
            List<TeamGamer> teamGamerList = teamGamerRepository.findAllByColorId(colorId);
            List<TeamGamerDTO> teamGamerDTOList = new ArrayList<>();
            for (TeamGamer teamGamer : teamGamerList) {
                teamGamerDTOList.add(new TeamGamerDTO(teamGamer));
            }
            return teamGamerDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }
}
