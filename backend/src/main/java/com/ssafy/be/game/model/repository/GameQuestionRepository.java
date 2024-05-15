package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.GameQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameQuestionRepository extends JpaRepository<GameQuestion, Integer> {
}
