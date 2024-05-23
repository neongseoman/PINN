package com.ssafy.be.gamer.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.domain.GamerLog;
import com.ssafy.be.gamer.model.dto.GamerLogDTO;
import com.ssafy.be.gamer.model.repository.GamerLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GamerLogServiceImpl implements GamerLogService {

    private final GamerLogRepository gamerLogRepository;

    @Autowired
    private GamerLogServiceImpl(GamerLogRepository gamerLogRepository) {
        this.gamerLogRepository = gamerLogRepository;
    }

    /////

    @Override
    public Long createGamerLog(GamerLogDTO gamerLogDTO) throws BaseException {
        try {

            GamerLog gamerLog = gamerLogDTO.toEntity();
            return gamerLogRepository.save(gamerLog).getGamerLogId();

        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public Long updateGamerLog(GamerLogDTO gamerLogDTO) throws BaseException {
        try {
            GamerLog existGamerLog = gamerLogRepository.findById(gamerLogDTO.getGamerLogId()).orElse(null);
            if (existGamerLog == null) throw new BaseException(BaseResponseStatus.OOPS);

            existGamerLog.setGamerId(gamerLogDTO.getGamerId());
            existGamerLog.setGameId(gamerLogDTO.getGameId());
            existGamerLog.setTeamId(gamerLogDTO.getTeamId());
            existGamerLog.setTotalRank(gamerLogDTO.getTotalRank());
            existGamerLog.setTeamColor(gamerLogDTO.getTeamColor());
            existGamerLog.setIsRoomLeader(gamerLogDTO.getIsRoomLeader());
            existGamerLog.setIsTeamLeader(gamerLogDTO.getIsTeamLeader());

            gamerLogRepository.save(existGamerLog);
            return existGamerLog.getGamerLogId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public Long deleteGamerLog(Long gamerLogId) throws BaseException {
        try {
            GamerLog existGamerLog = gamerLogRepository.findById(gamerLogId).orElse(null);
            if (existGamerLog == null) throw new BaseException(BaseResponseStatus.OOPS);

            gamerLogRepository.deleteById(gamerLogId);
            return gamerLogId;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public GamerLogDTO getGamerLog(Long gamerLogId) throws BaseException {
        try {
            GamerLog existGamerLog = gamerLogRepository.findById(gamerLogId).orElse(null);
            if (existGamerLog == null) {
                throw new BaseException(BaseResponseStatus.OOPS);
            } else {
                return new GamerLogDTO(existGamerLog);
            }
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<GamerLogDTO> getAllGamerLogByGamerId(int gamerId) throws BaseException {
        try {
            List<GamerLog> gamerLogList = gamerLogRepository.findAllByGamerId(gamerId);
            if (gamerLogList == null) {
                throw new BaseException(BaseResponseStatus.OOPS); // TODO: exception 타입 정의
            }

            List<GamerLogDTO> gamerLogDTOList = new ArrayList<>();
            for (GamerLog gamerLog : gamerLogList) {
                gamerLogDTOList.add(new GamerLogDTO(gamerLog));
            }
            return gamerLogDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }
}
