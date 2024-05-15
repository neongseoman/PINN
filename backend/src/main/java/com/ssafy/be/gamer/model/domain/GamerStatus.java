package com.ssafy.be.gamer.model.domain;

import jakarta.persistence.Entity;
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
public class GamerStatus {
    @Id
    private int gamerId;
    private int playCount;
    private int winCount;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
