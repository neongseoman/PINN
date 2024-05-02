package com.ssafy.be.common.model.domain;

import com.ssafy.be.common.component.GameComponent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;
    private String roomName;
    private int themeId;
    private int leaderId;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    @CreationTimestamp
    private LocalDateTime roomCreateTime;
    private LocalDateTime startedTime;
    @UpdateTimestamp
    private LocalDateTime finishedTime;
    private boolean hasPassword;

    public Game() {
    }

    public GameComponent toGameComponent() {
        return GameComponent.builder()
                .gameId(this.gameId)
                .roomName(roomName)
                .leaderId(this.leaderId)
                .roomCreateTime(this.roomCreateTime)
                .themeId(themeId)
                .stage1Time(this.stage1Time)
                .stage2Time(this.stage2Time)
                .roundCount(this.roundCount)
                .build();
    }
}
