package com.ssafy.be.game.model.dto.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.game.model.domain.Color;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ColorDTO {
    private int colorId;
    private String colorCode;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public ColorDTO(Color color) {
        this.colorId = color.getColorId();
        this.colorCode = color.getColorCode();
        this.createdDate = color.getCreatedDate();
        this.updatedDate = color.getUpdatedDate();
    }

    public Color toEntity() {
        return Color.builder()
                .colorId(this.colorId)
                .colorCode(this.colorCode)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .build();
    }
}
