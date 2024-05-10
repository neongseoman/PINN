package com.ssafy.be.gamer.repository;


import com.ssafy.be.gamer.model.GamerDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GamerRepository extends JpaRepository<GamerDTO, Object> {

    Optional<GamerDTO> findByProviderId(Long providerId);

    @Transactional
    @Modifying
    @Query("UPDATE GamerDTO g SET g.nickname = :nickname WHERE g.gamerId = :gamerId")
    void updateNicknameByGamerId(@Param("gamerId") int gamerId, @Param("nickname") String nickname);

}
