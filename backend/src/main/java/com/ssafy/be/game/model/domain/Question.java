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
public class Question {
    @Id
    private int questionId;
    private int themeId;
    private String questionName;
    private double lat;
    private double lng;
    private boolean useOrNot;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @CreationTimestamp
    private LocalDateTime updatedDate;

}
