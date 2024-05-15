package com.ssafy.be.gamer.repository;

import com.ssafy.be.gamer.model.domain.GamerLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamerLogRepository extends JpaRepository<GamerLog, Integer> {

}
