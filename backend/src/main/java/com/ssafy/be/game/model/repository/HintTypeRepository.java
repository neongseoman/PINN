package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.HintType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HintTypeRepository extends JpaRepository<HintType, Integer> {
}