package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.Hint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HintRepository extends JpaRepository<Hint, Integer> {
    List<Hint> findByUseOrNotAndQuestionId(int useOrNot, int questionId);
}
