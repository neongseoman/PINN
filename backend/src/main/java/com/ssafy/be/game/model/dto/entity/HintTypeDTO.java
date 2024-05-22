package com.ssafy.be.game.model.dto.entity;

import com.ssafy.be.game.model.domain.HintType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HintTypeDTO {
    private int hintTypeId;
    private String hintTypeName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public HintTypeDTO(HintType hintType) {
        this.hintTypeId = hintType.getHintTypeId();
        this.hintTypeName = hintType.getHintTypeName();
        this.createdDate = hintType.getCreatedDate();
        this.updatedDate = hintType.getUpdatedDate();
    }

    public HintType toEntity() {
        return HintType.builder()
                .hintTypeId(this.hintTypeId)
                .hintTypeName(this.hintTypeName)
                .build();
    }
}