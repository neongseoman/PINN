package com.ssafy.be.gamer.model.repository;

import com.ssafy.be.gamer.model.domain.GamerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamerStatusRepository extends JpaRepository<GamerStatus, Integer> {
}
