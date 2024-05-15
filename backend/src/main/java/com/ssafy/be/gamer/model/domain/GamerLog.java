package com.ssafy.be.gamer.model.domain;

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
public class GamerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gamerLogId;
    private int gamerId;
    private int gameId;
    private int teamId;
    private int rank;
    private String teamColor;
    private int isRoomLeader;
    private int isTeamLeader; // 이거 근데 필요한가?ㅋㅋ
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
