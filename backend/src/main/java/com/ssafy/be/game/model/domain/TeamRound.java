package com.ssafy.be.game.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TeamRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamRoundId;
    private int teamId;
    private int roundNumber;
    private int roundScore;
    private int submitStage;
    private LocalDateTime submitTime;
    private double submitLat;
    private double submitLng;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
