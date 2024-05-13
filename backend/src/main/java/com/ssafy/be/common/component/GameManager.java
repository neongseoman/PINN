package com.ssafy.be.common.component;

import static com.ssafy.be.common.response.BaseResponseStatus.ALREADY_START_GAME;
import static com.ssafy.be.common.response.BaseResponseStatus.FULL_ROOM_ERROR;
import static com.ssafy.be.common.response.BaseResponseStatus.FULL_TEAM_ERROR;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_GAME;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_UNREADY_TEAM;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_MATCH_PASSWORD;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.lobby.model.vo.EnterRoomVO;
import com.ssafy.be.room.model.dto.MoveTeamDTO;
import com.ssafy.be.lobby.model.vo.ExitRoomVO;
import com.ssafy.be.room.model.vo.MoveTeamVO;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map.Entry;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Objects;
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
            // 준비가 아직 안 된 & 팀 내 멤버가 3명 초과하지 않음
            if(!team.getValue().isReady()){
                // 팀멤버 객체가 없다면 생성
                if(team.getValue().getTeamGamers() == null){
                    team.getValue().setTeamGamers(new ConcurrentHashMap<>());
                } else if(team.getValue().getTeamGamers().size() == 3){
                    // 팀멤버 객체가 있다면 멤버가 3명이라면 넘어감
                    continue;
                }
                // 팀 내 멤버 수 + 1번째 (원래는 DB에 넣고 해당 key id를 넣어야 했음)
                int teamGamerNumber = team.getValue().getTeamGamers().size() + 1;
                TeamGamerComponent teamGamerComponent = TeamGamerComponent.builder()
                        .teamGamerNumber(teamGamerNumber)
                        .gamerId(gamerId)
                        .teamId(team.getValue().getTeamId())
                        .build();
                // 멤버를 팀에 삽입
                log.info(team.getValue().getTeamId() + "팀에 들어간 " + teamGamerComponent);
                team.getValue().getTeamGamers().put(gamerId, teamGamerComponent);
                return teamGamerComponent;
            }
        }

        //  들어갈 수 있는 공간이 없는 경우 Exception 처리
        throw new BaseException(FULL_ROOM_ERROR, gamerId);
    }

    // 방 나가기위한 method
    public ExitRoomVO exitRoom(SocketDTO socketDTO, GamerPrincipalVO gamerPrincipalVO) {

        // TODO : 각각의 경우 Exception 처리
        // game
        GameComponent gameComponent = games.get(socketDTO.getSenderGameId());
        // team
        TeamComponent teamComponent = gameComponent.teams.get(socketDTO.getSenderTeamId());
        // teamGamer
        // TeamComponent가 없다면 객체 생성
//        if(teamComponent == null){
//            teamComponent = new TeamComponent();
//        }
        ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = teamComponent.getTeamGamers();
        // TeamGamerComponent to ExitRoomVO
        TeamGamerComponent teamGamerComponent = teamGamers.get(gamerPrincipalVO.getGamerId());
        ExitRoomVO exitRoomVO = ExitRoomVO.builder()
                .senderDateTime(socketDTO.getSenderDateTime())
                .senderNickname(socketDTO.getSenderNickname())
                .senderGameId(socketDTO.getSenderGameId())
                .senderTeamId(socketDTO.getSenderTeamId())
                .senderTeamNumber(teamComponent.getTeamNumber())
                .code(1011)
                .msg(gamerPrincipalVO.getNickname() + "님이 " + socketDTO.getSenderGameId() + "번 방 " + socketDTO.getSenderTeamId() + "팀 " + teamComponent.getTeamNumber() + "번째 자리에서 나갔습니다.")
                .build();

        // remove gamer
        teamGamers.remove(gamerPrincipalVO.getGamerId());
        return exitRoomVO;
    }

    // 팀 옮기기위한 method
    public MoveTeamVO enterSpecificTeam(MoveTeamDTO moveTeamDTO, GamerPrincipalVO gamerPrincipalVO) {
        String nickname = moveTeamDTO.getSenderNickname();
        int newTeamNumber=0;

        // 게임
        GameComponent gameComponent = games.get(moveTeamDTO.getSenderGameId());
        // 팀
        ConcurrentHashMap<Integer, TeamComponent> teams = gameComponent.getTeams();
        TeamComponent newTeam = teams.get(moveTeamDTO.getNewTeamId());
        ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = newTeam.getTeamGamers();

        // TeamGamer가 null이라면
        if (teamGamers == null){
            newTeam.setTeamGamers(new ConcurrentHashMap<>());
            teamGamers = newTeam.getTeamGamers();
        }

        if(teamGamers.size() == 3){
            // 들어가려는 팀에 공간이 없다면 Exception
            throw new BaseException(FULL_TEAM_ERROR, gamerPrincipalVO.getGamerId());
        } else {
            // 공간이 있으므로 할당
            boolean[] teamPersonNumber = new boolean[4];
            for (TeamGamerComponent person: teamGamers.values()){
                teamPersonNumber[person.getTeamGamerNumber()] = true;
            }

            for (int i = 1; i < 4; i++) {
                if (!teamPersonNumber[i]){
                    newTeamNumber = i;
                    TeamGamerComponent teamGamerComponent = TeamGamerComponent.builder()
                            .gamerId(moveTeamDTO.getSenderGameId())
                            .teamId(moveTeamDTO.getNewTeamId())
                            .gamerId(gamerPrincipalVO.getGamerId())
                            .teamGamerNumber(newTeamNumber)
                            .build();
                    // 팀에 해당 게이머 삽입
                    teamGamers.put(gamerPrincipalVO.getGamerId(), teamGamerComponent);
                    break;
                }
            }
        }

        // VO 생성
        MoveTeamVO moveTeamVO = MoveTeamVO.builder()
                .oldTeamId(moveTeamDTO.getOldTeamId())
                .newTeamId(moveTeamDTO.getNewTeamId())
                .senderDateTime(moveTeamDTO.getSenderDateTime())
                .senderNickname(moveTeamDTO.getSenderNickname())
                .senderGameId(moveTeamDTO.getSenderGameId())
                .newTeamNumber(newTeamNumber)
                .code(1010)
                .msg(nickname + "님이 " + moveTeamDTO.getOldTeamId() + "팀에서 " + moveTeamDTO.getNewTeamId() + "팀 " + newTeamNumber + "번째로 이동했습니다.")
                .build();
        return moveTeamVO;
    }

    public GameComponent getGame(Integer gameId) {
        GameComponent game = games.getOrDefault(gameId, null);
        return game;
    }

    public boolean removeGame(Integer gameId){
        if (games.get(gameId) == null) {
            throw new BaseException(NOT_EXIST_GAME);
        }
        games.remove(gameId);
        return true;

    }

    public EnterRoomVO findFastestStartRoom(GamerPrincipalVO gamerPrincipalVO) {
        log.info("{} 빠른 입장 GameManager", gamerPrincipalVO.getGamerId());
        GameComponent gameComponent = null;
        int maxGamers = -1;

        for (GameComponent game : games.values()){
            if( game.getStatus() == GameStatus.READY){ // 시작 안한 게임
                boolean possibleTeam = false;
                boolean isPossibleGame = false;
                int totalGamers = 0;

                for (TeamComponent team : game.getTeams().values()) {
                    if (!team.isReady()) { // 팀이 준비가 아니라면 입장 가능.
                        possibleTeam = true;
                    }

                    if (team.getTeamGamers() != null) { // -> TeamGamer가 있다면
                        totalGamers += team.getTeamGamers().size();
                        if (team.getTeamGamers().size() == 3) possibleTeam = false; // 팀 정원을 채웠다면 팀에 참여 불가능.
                    }
                    if (possibleTeam) isPossibleGame = true; // 참여가능한 팀이 하나라도 있다면 참여 가능한 게임임.
                }
                if (totalGamers == 30) break; // 30명이라면 의미 없음.
                if (!isPossibleGame) continue; // 참여 가능한 게임이 아니라면 유효한 게임 컴포넌트로 보지 않겠음.

                if(totalGamers > maxGamers) {
                    maxGamers = totalGamers;
                    gameComponent = game;
                }
            }
        }

        if (gameComponent == null) {
            log.error("에러 발생: Game Component가 없음.");
            throw new BaseException(BaseResponseStatus.NOT_EXIST_READY_GAME);
        }


        TeamGamerComponent gamer = enterTeam(gameComponent, gamerPrincipalVO.getGamerId());

        return EnterRoomVO.builder()
                .senderTeamId(gamer.getTeamId())
                .senderGameId(gameComponent.getGameId())
                .senderDateTime(LocalDateTime.now())
                .senderNickname(gamerPrincipalVO.getNickname())
                .senderTeamNumber(gamer.getTeamGamerNumber())
                .code(1015)
                .msg("당신은 " + gamer.getTeamId() + "Team에 던져졌습니다.")
                .build();
    }
}
