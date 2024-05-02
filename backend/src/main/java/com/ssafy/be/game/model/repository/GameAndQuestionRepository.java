package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.GameAndQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameAndQuestionRepository extends JpaRepository<GameAndQuestion, Integer> {
}
