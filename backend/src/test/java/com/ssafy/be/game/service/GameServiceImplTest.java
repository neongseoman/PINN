package com.ssafy.be.game.service;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.component.TeamComponent;
import com.ssafy.be.lobby.model.dto.CreateRoomDTO;
import com.ssafy.be.common.component.GameStatus;
import com.ssafy.be.lobby.service.LobbyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Game Service Test")
@Nested
//@SpringBootTest // This annotation will load the Spring application context for testing
class GameServiceImplTest {
    @Mock
    private LobbyServiceImpl lobbyService;

    @InjectMocks
    private GameManager gameManager = new GameManager(); // Inject the mock dependency

    @BeforeEach
    void setUp() { // game 테스트할 수 있는 게임 방 만들기
        MockitoAnnotations.openMocks(this); // Initialize mocks
        ConcurrentHashMap<Integer, TeamComponent> teams = new ConcurrentHashMap<>();
        // Create a room using the mocked lobbyService
//        CreateRoomDTO createRoomDTO = new CreateRoomDTO(2, "테스트 방", 3, 30, 15, "abc123", 123);
        GameComponent testGameComponent = GameComponent.builder()
                .teams(teams)
                .gameId(123)
                .roomName("테스트 방")
                .themeId(2)
                .leaderId(1)
                .roundCount(3)
                .stage1Time(30)
                .stage2Time(15)
                .roomCreateTime(LocalDateTime.now())
                .startedTime(null) // Not started yet
                .finishedTime(null)
                .password("abc123")
                .status(GameStatus.READY)
                .questions(List.of(/* Add QuestionComponent objects here if needed */))
                .build();
        gameManager.addGame(testGameComponent);
    }

    @DisplayName("_test_startGame_existGame")
    @Test
    void _test_startGame_existGame() {
        //given
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();

        //when
        GameComponent existGame = games.get(123);

        //then
        assertEquals(existGame.getGameId(),123);
    }

    @Test
    void findGameInfo() {
    }

    @Test
    void findStage1Info() {
    }

    @Test
    void findStage2Info() {
    }
}