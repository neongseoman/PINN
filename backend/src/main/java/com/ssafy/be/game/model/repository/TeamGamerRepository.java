package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.TeamGamer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamGamerRepository extends JpaRepository<TeamGamer, Long> {
    List<TeamGamer> findAllByTeamId(int teamId);

    List<TeamGamer> findAllByGamerId(int gamerId);

//    List<TeamGamer> findAllByColorCode(String colorCode);

    List<TeamGamer> findAllByColorId(int colorId);
}
