package com.ssafy.be.game.model.dto.entitys;

import com.ssafy.be.game.model.domain.Theme;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
                .build();
    }
}
