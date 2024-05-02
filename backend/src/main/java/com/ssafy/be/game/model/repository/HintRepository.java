package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.Hint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HintRepository extends JpaRepository<Hint, Integer> {
}
