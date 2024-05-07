package com.ssafy.be.lobby.model.vo;

import com.ssafy.be.common.component.GameComponent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchVO {
    private List<GameComponent> ReadyGames;
}
