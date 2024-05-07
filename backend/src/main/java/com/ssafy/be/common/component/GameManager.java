package com.ssafy.be.common.component;

import static com.ssafy.be.common.response.BaseResponseStatus.ALREADY_START_GAME;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_GAME;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_UNREADY_TEAM;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_MATCH_PASSWORD;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import java.util.List;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class GameManager {
    // key : game_id
    private ConcurrentHashMap<Integer, GameComponent> games;

    public GameManager() {
        this.games = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer, GameComponent> getGames() {
        return games;
    }
    public boolean addGame(GameComponent game){
        if (games.getOrDefault(game.getGameId(), null) == null){
            games.put(game.getGameId(), game);
            return true;
        }
        return false;
    }

    /*
    * gameId가 GameManager에 존재하는 게임인지 확인하는 메서드
    * */
    public BaseResponse<?> isGame(Integer gameId, String password) {
        GameComponent gameComponent = games.getOrDefault(gameId, null);
//        log.info("server password" + gameComponent.getPassword());
        if (gameComponent == null){
            // 존재하지 않는 게임
            throw new BaseException(NOT_EXIST_GAME);
        }
        if (!gameComponent.getStatus().equals(GameStatus.READY)) {
            // 게임 실행 중인 방
            throw new BaseException(ALREADY_START_GAME);
        }
        if (gameComponent.getPassword() != null){
            // 비밀번호가 있는 방
            if(password == null || !password.equals(gameComponent.getPassword())){
                throw new BaseException(NOT_MATCH_PASSWORD);
            }
        }
        if(canEnterTeam(gameComponent) != null){
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }
        // 모든 팀이 준비 중
        throw new BaseException(NOT_EXIST_UNREADY_TEAM);
    }

    public Integer canEnterTeam(GameComponent gameComponent){
        for (Entry<Integer, TeamComponent> team : gameComponent.teams.entrySet()) {
            if(!team.getValue().isReady() || team.getValue().getTeamGamers()==null){
                // 준비 안 한 팀이 있거나 팀멤버 객체가 없다면 입장 가능
                return team.getKey();
            }
        }
        return null;
    }

    public TeamGamerComponent enterTeam(GameComponent gameComponent, Integer gamerId){
        for (Entry<Integer, TeamComponent> team : gameComponent.teams.entrySet()) {
            if(!team.getValue().isReady()){
                // 준비 안 한 팀이 있거나 팀멤버 객체가 없다면 입장 가능
                if(team.getValue().getTeamGamers() == null){
                    team.getValue().setTeamGamers(new ConcurrentHashMap<>());
                }
                // 팀 내 멤버 수 + 1번째 (원래는 DB에 넣고 해당 key id를 넣어야 했음)
                Long teamGamerNumber = (long) team.getValue().getTeamGamers().size() + 1;
                TeamGamerComponent teamGamerComponent = TeamGamerComponent.builder()
                        .teamGamerId(teamGamerNumber)
                        .gamerId(gamerId)
                        .teamId(team.getValue().getTeamId())
                        .build();
                // 멤버를 팀에 삽입
                log.info(team.getValue().getTeamId() + "팀에 들어간 " + teamGamerComponent);
                team.getValue().getTeamGamers().put(teamGamerNumber, teamGamerComponent);
                return teamGamerComponent;
            }
        }
        return null;
    }
}
