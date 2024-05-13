package com.ssafy.be.game.model.dto.entity;

import com.ssafy.be.game.model.domain.Theme;
import lombok.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThemeDTO {
    private int themeId;
    private String themeName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public ThemeDTO(Theme theme) {
        this.themeId = theme.getThemeId();
        this.themeName = theme.getThemeName();
        this.createdDate = theme.getCreatedDate();
        this.updatedDate = theme.getUpdatedDate();
    }

    public Theme toEntity() {
        return Theme.builder()
                .themeId(this.themeId)
                .themeName(this.themeName)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .build();
    }
}
