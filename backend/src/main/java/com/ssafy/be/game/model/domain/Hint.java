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
public class Hint {
    @Id
    private int hintId;
    private int questionId;
    private int hintTypeId;
    private String hintValue;
    private int offerStage;
    private int useOrNot;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @CreationTimestamp
    private LocalDateTime updatedDate;
}
