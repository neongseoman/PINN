package com.ssafy.be.gamer.repository;

import com.ssafy.be.gamer.model.LoginTokenDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamerLoginRedisRepository extends CrudRepository<LoginTokenDTO,String> {
}
