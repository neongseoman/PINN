package com.ssafy.be.game.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameAndQuestion {
    @Id
    private int gameAndQuestionId;
    private int gameId;
    private int questionId;
    private int roundNumber;
    @CreationTimestamp
    private LocalDateTime createdDate;
}
