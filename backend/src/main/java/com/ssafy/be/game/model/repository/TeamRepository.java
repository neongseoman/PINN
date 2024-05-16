package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findAllByGameId(int gameId);
}
