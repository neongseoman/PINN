package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.TeamGamer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamGamerRepository extends JpaRepository<TeamGamer, Long> {
}
