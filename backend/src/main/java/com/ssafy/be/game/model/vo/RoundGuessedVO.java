package com.ssafy.be.game.model.vo;

import com.ssafy.be.game.model.dto.TeamPinDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoundGuessedVO {
    private int gameId;
    private List<TeamPinDTO> teamPins;
}
