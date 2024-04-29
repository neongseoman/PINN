package com.ssafy.be.gamer.repository;


import com.ssafy.be.gamer.model.GamerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GamerRepository extends JpaRepository<GamerDTO, Object> {

    Optional<GamerDTO> findByProviderId(Long providerId);
}
