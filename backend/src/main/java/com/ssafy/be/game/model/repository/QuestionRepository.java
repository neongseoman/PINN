package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByUsedAndThemeId(int used, int themeId);
}
