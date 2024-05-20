package com.ssafy.be.game.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hintId;
    private int questionId;
    private int hintTypeId;
    private String hintValue;
    private int offerStage;
    private int used;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @CreationTimestamp
    private LocalDateTime updatedDate;
}
