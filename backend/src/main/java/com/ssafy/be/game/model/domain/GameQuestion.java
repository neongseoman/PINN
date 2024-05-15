package com.ssafy.be.game.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "game-question")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameQuestion {
    @Id
    private int gameQuestionId;
    private int gameId;
    private int questionId;
    private int roundNumber;
    @CreationTimestamp
    private LocalDateTime createdDate;
}
