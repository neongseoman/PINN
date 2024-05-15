package com.ssafy.be.gamer.model.dto;

import com.ssafy.be.gamer.model.domain.GamerStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GamerStatusDTO {
    private int gamerId;
    private int playCount;
    private int winCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public GamerStatusDTO(GamerStatus gamerStatus) {
        this.gamerId = gamerStatus.getGamerId();
        this.playCount = gamerStatus.getPlayCount();
        this.winCount = gamerStatus.getWinCount();
        this.createdDate = gamerStatus.getCreatedDate();
        this.updatedDate = gamerStatus.getUpdatedDate();
    }

    public GamerStatus toEntity() {
        return GamerStatus.builder()
                .gamerId(this.gamerId)
                .playCount(this.playCount)
                .winCount(this.winCount)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .build();
    }
}
