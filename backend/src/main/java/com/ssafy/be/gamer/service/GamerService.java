package com.ssafy.be.gamer.service;

import com.ssafy.be.gamer.model.GamerDTO;
import com.ssafy.be.gamer.model.dto.NicknameRequestDTO;
import com.ssafy.be.gamer.model.repository.GamerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GamerService {
    private final GamerRepository gamerRepository;

    public void getGamerRepository(GamerDTO gamerDTO) {
        gamerRepository.save(gamerDTO);
    }
    public void updateNickname(int gamerId, NicknameRequestDTO nicknameRequestDTO) {
        gamerRepository.updateNicknameByGamerId(gamerId,nicknameRequestDTO.getNickname());
    }
}
