package com.ssafy.be.gamer.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.domain.GamerStatus;
import com.ssafy.be.gamer.model.dto.GamerStatusDTO;
import com.ssafy.be.gamer.repository.GamerStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GamerStatusServiceImpl implements GamerStatusService {

    private final GamerStatusRepository gamerStatusRepository;

    @Autowired
    private GamerStatusServiceImpl(GamerStatusRepository gamerStatusRepository) {
        this.gamerStatusRepository = gamerStatusRepository;
    }

    /////


    @Override
    public int createGamerStatus(GamerStatusDTO gamerStatusDTO) throws BaseException {
        try {
            // TODO:
            // GamerStatus create하는 경우 항상 count값 0, 0일 것이므로, 굳이 DTO로 안 받아도 될 수도 있음.
            // 이 부분 생각해 보기.
            GamerStatus gamerStatus = gamerStatusDTO.toEntity();
            return gamerStatusRepository.save(gamerStatus).getGamerId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public int updateGamerStatus(GamerStatusDTO gamerStatusDTO) throws BaseException {
        try {
            GamerStatus existGamerStatus = gamerStatusRepository.findById(gamerStatusDTO.getGamerId()).orElse(null);
            if (existGamerStatus == null) throw new BaseException(BaseResponseStatus.OOPS);

//            existGamerStatus.setGamerId(gamerStatusDTO.getGamerId()); // id 변경하는 행위. 일단 주석 처리
            existGamerStatus.setPlayCount(gamerStatusDTO.getPlayCount());
            existGamerStatus.setWinCount(gamerStatusDTO.getWinCount());

            return gamerStatusRepository.save(existGamerStatus).getGamerId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public int deleteGamerStatus(int gamerId) throws BaseException {
        try {
            GamerStatus existGamerStatus = gamerStatusRepository.findById(gamerId).orElse(null);
            if (existGamerStatus == null) throw new BaseException(BaseResponseStatus.OOPS);

            gamerStatusRepository.deleteById(gamerId);
            return gamerId;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public GamerStatusDTO getGamerStatusByGamerId(int gamerId) throws BaseException {
        try {
            GamerStatus existGamerStatus = gamerStatusRepository.findById(gamerId).orElse(null);
            if (existGamerStatus == null) {
                throw new BaseException(BaseResponseStatus.OOPS);
            } else {
                return new GamerStatusDTO(existGamerStatus);
            }
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<GamerStatusDTO> getAllGamerStatus() throws BaseException {
        try {
            List<GamerStatus> gamerStatusList = gamerStatusRepository.findAll();
            List<GamerStatusDTO> gamerStatusDTOList = new ArrayList<>();
            for (GamerStatus gamerStatus : gamerStatusList) {
                gamerStatusDTOList.add(new GamerStatusDTO(gamerStatus));
            }
            return gamerStatusDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public GamerStatusDTO updateWinAndPlayCount(int gamerId) throws BaseException {
        try {
            GamerStatus existGamerStatus = gamerStatusRepository.findById(gamerId).orElse(null);
            if (existGamerStatus == null) throw new BaseException(BaseResponseStatus.OOPS);

            // wincount++, playcount++
            existGamerStatus.setWinCount(existGamerStatus.getWinCount() + 1);
            existGamerStatus.setPlayCount(existGamerStatus.getPlayCount() + 1);
            gamerStatusRepository.save(existGamerStatus);

            return new GamerStatusDTO(existGamerStatus);
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public GamerStatusDTO updatePlayCount(int gamerId) throws BaseException {
        try {
            GamerStatus existGamerStatus = gamerStatusRepository.findById(gamerId).orElse(null);
            if (existGamerStatus == null) throw new BaseException(BaseResponseStatus.OOPS);

            // playcount++
            existGamerStatus.setPlayCount(existGamerStatus.getPlayCount() + 1);
            gamerStatusRepository.save(existGamerStatus);

            return new GamerStatusDTO(existGamerStatus);
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }


}
