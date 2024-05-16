package com.ssafy.be.common.component;

import static com.ssafy.be.common.Provider.ColorCode.NEON_GREEN;
import static com.ssafy.be.common.Provider.ColorCode.NEON_PINK;
import static com.ssafy.be.common.Provider.ColorCode.NEON_YELLOW;
import static com.ssafy.be.common.response.BaseResponseStatus.ALREADY_START_GAME;
import static com.ssafy.be.common.response.BaseResponseStatus.FULL_ROOM_ERROR;
import static com.ssafy.be.common.response.BaseResponseStatus.FULL_TEAM_ERROR;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_GAME;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_GAMER;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_LEADER;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_TEAM;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_UNREADY_TEAM;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_MATCH_PASSWORD;

import com.ssafy.be.common.Provider.ColorCode;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.lobby.model.ReadyGame;
import com.ssafy.be.lobby.model.SearchTeam;
import com.ssafy.be.lobby.model.vo.EnterRoomVO;
import com.ssafy.be.lobby.service.LobbyService;
import com.ssafy.be.lobby.service.LobbyServiceImpl;
import com.ssafy.be.room.model.dto.MoveTeamDTO;
import com.ssafy.be.lobby.model.vo.ExitRoomVO;
import com.ssafy.be.room.model.dto.RoomStatusDTO;
import com.ssafy.be.room.model.vo.MoveTeamVO;

import com.ssafy.be.room.model.vo.RoomStatusVO;
import com.ssafy.be.room.model.vo.TeamStatusVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean addGame(GameComponent game) {
        if (games.getOrDefault(game.getGameId(), null) == null) {
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
        if (gameComponent == null) {
            // 존재하지 않는 게임
            throw new BaseException(NOT_EXIST_GAME);
        }
        if (!gameComponent.getStatus().equals(GameStatus.READY)) {
            // 게임 실행 중인 방
            throw new BaseException(ALREADY_START_GAME);
        }
        if (gameComponent.getPassword() != null) {
            // 비밀번호가 있는 방
            if (password == null || !password.equals(gameComponent.getPassword())) {
                throw new BaseException(NOT_MATCH_PASSWORD);
            }
        }
        if (canEnterTeam(gameComponent) != null) {
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }
        // 모든 팀이 준비 중
        throw new BaseException(NOT_EXIST_UNREADY_TEAM);
    }

    public Integer canEnterTeam(GameComponent gameComponent) {
        for (Entry<Integer, TeamComponent> team : gameComponent.teams.entrySet()) {
            if (!team.getValue().isReady() || team.getValue().getTeamGamers() == null) {
                // 준비 안 한 팀이 있거나 팀멤버 객체가 없다면 입장 가능
                return team.getKey();
            }
        }
        return null;
    }

    public TeamGamerComponent enterTeam(GameComponent gameComponent, GamerPrincipalVO gamerPrincipalVO) {
        for (Entry<Integer, TeamComponent> team : gameComponent.teams.entrySet()) {
            // 준비가 아직 안 된 & 팀 내 멤버가 3명 초과하지 않음
            if (!team.getValue().isReady()) {
                // 팀게이머 객체가 없다면 생성
                ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = team.getValue().getTeamGamers();
                if (teamGamers == null) {
                    team.getValue().setTeamGamers(new ConcurrentHashMap<>());
                    teamGamers = team.getValue().teamGamers;
                } else if (teamGamers.size() == 3) {
                    // 팀게이머 객체가 있고 멤버가 3명이라면 넘어감
                    continue;
                }
                // 팀 내 번호 할당 (원래는 DB에 넣고 해당 key id를 넣어야 했음)
                int teamGamerNumber = getTeamGamerNumber(teamGamers);
                if (teamGamerNumber == 0) {
                    // TODO : 팀 내에 제대로 할당받지 못 함 (사람이 모두 찼을 경우)
//                    throw new BaseException();
                }

                // 색상 할당
                ColorCode teamGamerColor = null;
                if (teamGamerNumber == 1) {
                    teamGamerColor = NEON_GREEN;
                } else if (teamGamerNumber == 2) {
                    teamGamerColor = NEON_PINK;
                } else {
                    teamGamerColor = NEON_YELLOW;
                }

                TeamGamerComponent teamGamerComponent = TeamGamerComponent.builder()
                        .teamGamerNumber(teamGamerNumber)
                        .gamerId(gamerPrincipalVO.getGamerId())
                        .teamId(team.getValue().getTeamId())
                        .nickname(gamerPrincipalVO.getNickname())
                        .teamGamerColor(teamGamerColor.getColorCode())
                        .build();
                // 멤버를 팀에 삽입
                log.info(team.getValue().getTeamId() + "팀에 들어간 " + teamGamerComponent);
                team.getValue().getTeamGamers().put(gamerPrincipalVO.getGamerId(), teamGamerComponent);
                return teamGamerComponent;
            }
        }

        //  들어갈 수 있는 공간이 없는 경우 Exception 처리
        throw new BaseException(FULL_ROOM_ERROR, gamerPrincipalVO.getGamerId());
    }

    private int getTeamGamerNumber(ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers) {
        boolean[] checkTeamGamerNumber = new boolean[4];

        for (int i = 1; i < 4; i++) {
            if (teamGamers.getOrDefault(i, null) == null) {
                // 할당할 번호 찾음
                return i;
            }
        }
        return 0;
    }

    // 방 나가기위한 method
    public ExitRoomVO exitRoom(SocketDTO socketDTO, GamerPrincipalVO gamerPrincipalVO, boolean moveTeam) {
        // TODO : 방 내 모든 사람이 나간경우 삭제
        // TODO : 각각의 경우 Exception 처리
        // game
        GameComponent gameComponent = games.get(socketDTO.getSenderGameId());
        if (gameComponent == null) {
            throw new BaseException(NOT_EXIST_GAME, gamerPrincipalVO.getGamerId());
        }
        // team
        TeamComponent teamComponent = gameComponent.teams.get(socketDTO.getSenderTeamId());
        // teamGamer
        ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = teamComponent.getTeamGamers();
        if (teamGamers == null) {
            throw new BaseException(NOT_EXIST_GAMER, gamerPrincipalVO.getGamerId());
        }
        // TeamGamerComponent to ExitRoomVO
        TeamGamerComponent teamGamerComponent = teamGamers.get(gamerPrincipalVO.getGamerId());
        ExitRoomVO exitRoomVO = null;

        // 팀을 옮기는 경우가 아님 & 나가는 사람이 리더라면
        if (!moveTeam && gamerPrincipalVO.getGamerId() == gameComponent.getLeaderId()) {
            // 새로운 리더 받아오기
            TeamGamerComponent newLeader = getNewLeader(gameComponent);
            // 리더로 할당할 사람이 없다면
            if (newLeader == null) {
                // 게임 삭제
                getGames().remove(socketDTO.getSenderGameId());
                throw new BaseException(NOT_EXIST_LEADER, gamerPrincipalVO.getGamerId());
            }

            // 새로운 리더 아이디를 방에 할당
            gameComponent.setLeaderId(newLeader.getGamerId());

            exitRoomVO = ExitRoomVO.builder()
                    .senderDateTime(socketDTO.getSenderDateTime())
                    .senderNickname(socketDTO.getSenderNickname())
                    .senderGameId(socketDTO.getSenderGameId())
                    .senderTeamId(socketDTO.getSenderTeamId())
                    .changeLeader(true)
                    .newLeaderNickname(newLeader.getNickname())
                    .senderTeamNumber(teamComponent.getTeamNumber())
                    .code(1012)
                    .msg(gamerPrincipalVO.getNickname() + "님이 " + socketDTO.getSenderGameId() + "번 방 " + socketDTO.getSenderTeamId() + "팀 " + teamComponent.getTeamNumber() + "번째 자리에서 나갔습니다.")
                    .build();
        }
        // 팀을 옮기는 경우 | 리더가 아니라면
        else {
            exitRoomVO = ExitRoomVO.builder()
                    .senderDateTime(socketDTO.getSenderDateTime())
                    .senderNickname(socketDTO.getSenderNickname())
                    .senderGameId(socketDTO.getSenderGameId())
                    .senderTeamId(socketDTO.getSenderTeamId())
                    .senderTeamNumber(teamComponent.getTeamNumber())
                    .changeLeader(false)
                    .code(1011)
                    .msg(gamerPrincipalVO.getNickname() + "님이 " + socketDTO.getSenderGameId() + "번 방 " + socketDTO.getSenderTeamId() + "팀 " + teamComponent.getTeamNumber() + "번째 자리에서 나갔습니다.")
                    .build();
        }

        // remove gamer
        teamGamers.remove(gamerPrincipalVO.getGamerId());
        // 방에 아무도 없는 경우 확인
        if (checkRoomEmpty(socketDTO.getSenderGameId()))
            removeGame(socketDTO.getSenderGameId());

        return exitRoomVO;
    }

    // 모든 TeamComponenet를 순회하면서 유저 상황을 체크하ㅡㄴ 것이 최선인가?
    private boolean checkRoomEmpty(int gameId) {
        GameComponent gameComponent = games.get(gameId);
        int count = 0;
        ConcurrentHashMap<Integer, TeamComponent> teams = gameComponent.getTeams();
        for (TeamComponent team : teams.values()) {
            if (team.getTeamGamers() != null) {
                count += team.getTeamGamers().size();
            }
        }

        if (count == 0) return true;
        return false;
    }

    // 리더가 될 사람을 찾는 메서드
    private TeamGamerComponent getNewLeader(GameComponent gameComponent) {
        TeamGamerComponent newLeader = null;

        for (TeamComponent teamComponent : gameComponent.getTeams().values()) {
            ConcurrentHashMap<Integer, TeamGamerComponent> teamGamerComponent = teamComponent.getTeamGamers();
            // 팀 내 사람이 있다면
            if (teamGamerComponent != null && !teamGamerComponent.isEmpty()) {
                for (int i = 1; i <= 3; i++) {
                    // 팀 내 앞에서부터 존재하는 사람 찾음
                    newLeader = teamGamerComponent.getOrDefault(i, null);
                    if (newLeader != null) {
                        return newLeader;
                    }
                }
            }
        }
        return newLeader;
    }

    // 팀 옮기기위한 method
    public MoveTeamVO enterSpecificTeam(MoveTeamDTO moveTeamDTO, GamerPrincipalVO gamerPrincipalVO) {
        String nickname = moveTeamDTO.getSenderNickname();
        int newTeamNumber = 0;

        // 게임
        GameComponent gameComponent = games.get(moveTeamDTO.getSenderGameId());
        // 팀
        ConcurrentHashMap<Integer, TeamComponent> teams = gameComponent.getTeams();
        TeamComponent newTeam = teams.get(moveTeamDTO.getNewTeamId());
        ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = newTeam.getTeamGamers();

        // TeamGamer가 null이라면
        if (teamGamers == null) {
            newTeam.setTeamGamers(new ConcurrentHashMap<>());
            teamGamers = newTeam.getTeamGamers();
        }

        if (teamGamers.size() == 3) {
            // 들어가려는 팀에 공간이 없다면 Exception
            throw new BaseException(FULL_TEAM_ERROR, gamerPrincipalVO.getGamerId());
        } else {
            // 공간이 있으므로 할당
            boolean[] teamPersonNumber = new boolean[4];
            for (TeamGamerComponent person : teamGamers.values()) {
                teamPersonNumber[person.getTeamGamerNumber()] = true;
            }

            for (int i = 1; i < 4; i++) {
                if (!teamPersonNumber[i]) {
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

    public ReadyGame getGame(Integer gameId) {
        GameComponent gameComponent = games.getOrDefault(gameId, null);
        if (gameComponent == null) {
            throw new BaseException(NOT_EXIST_GAME);
        }

        List<SearchTeam> teams = getTeams(gameComponent);
        boolean isPassword = true;
        if (gameComponent.getPassword() == null || gameComponent.getPassword().isEmpty()) {
            isPassword = false;
        }

        return ReadyGame.builder()
                .teams(teams)
                .roomName(gameComponent.getRoomName())
                .gameId(gameComponent.getGameId())
                .themeId(gameComponent.getThemeId())
                .leaderId(gameComponent.getLeaderId())
                .roundCount(gameComponent.getRoundCount())
                .stage1Time(gameComponent.getStage1Time())
                .stage2Time(gameComponent.getStage2Time())
                .password(isPassword)
                .status(gameComponent.getStatus())
                .build();
    }

    public List<SearchTeam> getTeams(GameComponent gameComponent) {
        List<SearchTeam> teams = new ArrayList<>();
        for (TeamComponent teamComponent : gameComponent.getTeams().values()) {
            SearchTeam searchTeam = SearchTeam.builder()
                    .teamGamers(new ArrayList<>())
                    .teamId(teamComponent.getTeamId())
                    .gameId(teamComponent.getGameId())
                    .colorCode(teamComponent.getColorCode())
                    .teamNumber(teamComponent.getTeamNumber())
                    .isReady(teamComponent.isReady())
                    .lastReadyTime(teamComponent.getLastReadyTime())
                    .build();

            ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = teamComponent.getTeamGamers();
            if (teamGamers != null) {
                searchTeam.getTeamGamers().addAll(teamComponent.getTeamGamers().values());
            }

            teams.add(searchTeam);
        }

        return teams;
    }

    public boolean removeGame(Integer gameId) {
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

        for (GameComponent game : games.values()) {
            if (game.getPassword() != null && !game.getPassword().isEmpty()) continue;
            if (game.getStatus() == GameStatus.READY) { // 시작 안한 게임
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
                if (totalGamers == 30) continue; // 30명이라면 의미 없음.
                if (!isPossibleGame) continue; // 참여 가능한 게임이 아니라면 유효한 게임 컴포넌트로 보지 않겠음.

                if (totalGamers > maxGamers) {
                    maxGamers = totalGamers;
                    gameComponent = game;
                }
            }
        }

        if (gameComponent == null) {
            log.error("에러 발생: Game Component가 없음.");
            throw new BaseException(BaseResponseStatus.NOT_EXIST_READY_GAME);
        }
        ;
        log.info("빠른 시작 Game Id : {}", gameComponent.getGameId());

        TeamComponent teamComponent;
        try {

            teamComponent = gameComponent.getTeams().values().stream()
                    .filter(team -> !team.isReady()).findFirst().orElseThrow();

        } catch (NoSuchElementException e) {
            throw new BaseException(BaseResponseStatus.NOT_EXIST_VALIE_TEAM);
        }
        enterTeam(gameComponent, gamerPrincipalVO);
        TeamGamerComponent gamer = enterTeam(gameComponent, gamerPrincipalVO);

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

    public TeamStatusVO changeTeamStatus(SocketDTO socketDTO, GamerPrincipalVO gamerPrincipalVO) {
        // 룸(게임)
        GameComponent gameComponent = games.get(socketDTO.getSenderGameId());
        if (gameComponent == null) {
            throw new BaseException(NOT_EXIST_GAME, gamerPrincipalVO.getGamerId());
        }
        // 팀
        TeamComponent teamComponent = gameComponent.getTeams().get(socketDTO.getSenderTeamId());
        if (teamComponent == null) {
            throw new BaseException(NOT_EXIST_TEAM, gamerPrincipalVO.getGamerId());
        }

        // 상태 변경
        teamComponent.setReady(!teamComponent.isReady());
        // VO 생성
        TeamStatusVO teamStatusVO = TeamStatusVO.builder()
                .senderDateTime(socketDTO.getSenderDateTime())
                .teamStatus(teamComponent.isReady())
                .senderGameId(socketDTO.getSenderGameId())
                .senderNickname(socketDTO.getSenderNickname())
                .senderTeamId(socketDTO.getSenderTeamId())
                .code(1026)
                .msg(teamComponent.getTeamId() + "팀 상태가 " + !teamComponent.isReady() + "에서 " + teamComponent.isReady() + "로 변경되었습니다.")
                .build();

        return teamStatusVO;
    }

    public RoomStatusVO changeRoomStatus(RoomStatusDTO roomStatusDTO) {
        log.info("{} : room status is changed " ,roomStatusDTO.getSenderGameId());
        GameComponent gameComponent = games.get(roomStatusDTO.getSenderGameId());
        gameComponent.setStage1Time(roomStatusDTO.getStage1());
        gameComponent.setStage2Time(roomStatusDTO.getStage2());
        gameComponent.setThemeId(roomStatusDTO.getThemeId());
        gameComponent.setRoundCount(roomStatusDTO.getRound());

        return RoomStatusVO.builder()
                .code(1027)
                .msg("방의 상태가 바뀌었습니다.")
                .build();
    }
}
