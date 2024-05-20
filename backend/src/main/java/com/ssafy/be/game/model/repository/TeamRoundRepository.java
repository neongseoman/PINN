package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.TeamRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRoundRepository extends JpaRepository<TeamRound, Long> {
    List<TeamRound> findAllByTeamId(int teamId);
}
