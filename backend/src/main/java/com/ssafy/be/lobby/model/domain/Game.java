package com.ssafy.be.lobby.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Game {
    @Id
    int game_id;

}
