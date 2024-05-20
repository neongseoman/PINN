package com.ssafy.be.lobby.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class CreateRoomDTO {
    private int themeId;
    private String roomName;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    private String password;
    private int leader_id;

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }
}