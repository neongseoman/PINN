package com.ssafy.be.gamer.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@Setter
@RequiredArgsConstructor
public class NicknameRequestDTO {
    String nickname;
}
