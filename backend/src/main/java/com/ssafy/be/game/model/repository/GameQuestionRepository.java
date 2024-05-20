package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.GameQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameQuestionRepository extends JpaRepository<GameQuestion, Integer> {
    List<GameQuestion> findAllByGameId(int gameId);

    List<GameQuestion> findAllByQuestionId(int questionId);
}
