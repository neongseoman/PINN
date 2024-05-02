package com.ssafy.be.gamer.repository;

import com.ssafy.be.gamer.model.GamerDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamerRedisRepository extends CrudRepository<GamerDTO, Long> {
}
