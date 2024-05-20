package com.ssafy.be.game.model.repository;

import com.ssafy.be.game.model.domain.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Integer> {
}
