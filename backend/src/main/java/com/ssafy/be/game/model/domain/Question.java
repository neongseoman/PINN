package com.ssafy.be.game.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

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
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
