package com.ssafy.be.gamer.repository;

import com.ssafy.be.gamer.model.domain.GamerLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamerLogRepository extends JpaRepository<GamerLog, Long> {

    List<GamerLog> findAllByGamerId(int gamerId);
}
