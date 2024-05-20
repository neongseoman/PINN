package com.ssafy.be.room.model.vo;

import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RoomStatusVO{
    private int code;
    private String msg;

}
