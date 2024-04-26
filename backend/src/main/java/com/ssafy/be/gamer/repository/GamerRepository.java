package com.ssafy.be.gamer.repository;


import com.ssafy.be.gamer.model.GamerDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GamerRepository extends JpaRepository<GamerDTO, Object> {
    Optional<GamerDTO> findByEmailAndOAuthProvider(String username,String provider);
}
