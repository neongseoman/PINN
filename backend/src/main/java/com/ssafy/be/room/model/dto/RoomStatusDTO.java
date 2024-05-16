package com.ssafy.be.room.model.dto;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RoomStatusDTO extends SocketDTO {
    private int themeId;
    private int round;
    private int stage1;
    private int stage2;
}
