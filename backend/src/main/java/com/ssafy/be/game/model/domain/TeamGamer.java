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
public class TeamGamer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamGamerId;
    private int teamId;
    //    private int colorId; // TODO: 이거 진짜 string으로 해도 되는 건지 확인 필요
    private String colorCode; // teamGamerComponent를 따라감...
    private int gamerId;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
