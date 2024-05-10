//package com.ssafy.be.game.service;
//
//import com.ssafy.be.common.component.*;
//import com.ssafy.be.common.exception.BaseException;
//import com.ssafy.be.common.model.domain.Game;
//import com.ssafy.be.common.response.BaseResponseStatus;
//import com.ssafy.be.game.model.dto.GameStartRequestDTO;
//import com.ssafy.be.game.model.dto.RoundFinishRequestDTO;
//import com.ssafy.be.game.model.vo.RoundFinishVO;
//import com.ssafy.be.lobby.model.dto.CreateRoomDTO;
//import com.ssafy.be.lobby.service.LobbyServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//@DisplayName("Game Service Test")
//@Nested
////@SpringBootTest // This annotation will load the Spring application context for testing
//class GameServiceImplTest {
//    private static final Logger log = LoggerFactory.getLogger(GameServiceImplTest.class);
//    @Mock
//    private LobbyServiceImpl lobbyService;
//
//    @Mock
//    private GameService gameService;
//
//    @InjectMocks
//    private GameManager gameManager = new GameManager(); // Inject the mock dependency
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        // Mocking the GameManager to return the predefined mock game
//        GameComponent mockGame = MockGameSetup.setupMockGameWithTeams();
//        ConcurrentHashMap<Integer, GameComponent> games = new ConcurrentHashMap<>();
//        games.put(mockGame.getGameId(), mockGame);
//        when(gameManager.getGames()).thenReturn(games);
//    }
//
//    @DisplayName("_test_startGame_existGame_is_not")
//    @Test
//    void _test_startGame_existGameIsNot() {
//        // Arrange: Create a request with a game ID that doesn't exist
//        GameStartRequestDTO gameStartRequestDTO =
//                new GameStartRequestDTO("testUser", 1,
//                        1, 1, 1, 2, 3, 4);
//        RoundFinishRequestDTO finishRequestDTO = new RoundFinishRequestDTO(
//                gameStartRequestDTO.getSenderNickname(),
//                gameStartRequestDTO.getSenderGameId(),
//                gameStartRequestDTO.getSenderTeamId(),
//                1
//        );
//
//        // Set up the gameService mock to throw an exception when finishRound is called with this request
//        doThrow(new BaseException(BaseResponseStatus.NOT_EXIST_GAME)).when(gameService).finishRound(any(RoundFinishRequestDTO.class));
//
//        // Act & Assert: Verify that the BaseException is thrown
//        BaseException thrownException = assertThrows(BaseException.class, () -> {
//            gameService.finishRound(finishRequestDTO);
//        });
//
//        // Ensure the exception is thrown for the correct reason
//        assertEquals(BaseResponseStatus.NOT_EXIST_GAME, thrownException.getStatus());
//    }
//
//
//    @DisplayName("Throws BaseException when game does not exist")
//    @Test
//    void finishRound_GameNotFound() {
//        // Arrange: Create a request with a non-existent game ID
//        RoundFinishRequestDTO request = new RoundFinishRequestDTO("tester", 999, 1, 1);
//
//        // Act & Assert: Verify that BaseException is thrown
//        BaseException exception = assertThrows(BaseException.class, () -> gameService.finishRound(request));
//
//        // Check if the correct status code is included
//        assertEquals(BaseResponseStatus.NOT_EXIST_GAME, exception.getStatus());
//    }
//
//    @DisplayName("Successfully processes an existing game")
//    @Test
//    void finishRound_ExistingGame() {
//        // Arrange: Create a request with an existing game ID
//        RoundFinishRequestDTO request = new RoundFinishRequestDTO("tester", 123, 1, 1);
//
//        // Act: Run the finishRound method and capture the result
//        RoundFinishVO result = gameService.finishRound(request);
//
//        // Assert: Check if the result is not null and contains expected data (modify based on your requirements)
//        assertNotNull(result, "RoundFinishVO should not be null");
//        // Add more assertions to verify the state and data in result
//    }
//    public class MockGameSetup {
//        public static GameComponent setupMockGameWithTeams() {
//            // Create team gamers
//            ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = new ConcurrentHashMap<>();
//            teamGamers.put(1, TeamGamerComponent.builder()
//                    .teamId(1)
//                    .colorId(1)
//                    .gamerId(1)
//                    .teamGamerNumber(1)
//                    .build());
//            teamGamers.put(2, TeamGamerComponent.builder()
//                    .teamId(1)
//                    .colorId(1)
//                    .gamerId(2)
//                    .teamGamerNumber(2)
//                    .build());
//
//            // Create mock team rounds
//            ConcurrentHashMap<Integer, TeamRoundComponent> teamRounds = new ConcurrentHashMap<>();
//            TeamRoundComponent teamRound = new TeamRoundComponent();
//            teamRound.setRoundNumber(1); // roundNumber 설정
//            teamRound.setSubmitLat(-1);
//            teamRound.setSubmitLng(-1); // 최초 핀찍기 이전 위, 경도 초기값 기본 "-1"으로 설정
//            teamRound.setGuessed(false); // guessed(답 제출 여부) 기본 "false"로 설정
//            teamRounds.put(1, teamRound);
//            // Create a team and populate it with gamers and rounds
//            TeamComponent team = TeamComponent.builder()
//                    .teamId(1)
//                    .gameId(123)
//                    .colorCode("rgba(251, 52, 159, 1)")
//                    .teamNumber(1)
//                    .isReady(true)
//                    .lastReadyTime(LocalDateTime.now())
//                    .finalRank(0)
//                    .finalScore(0)
//                    .teamGamers(teamGamers)
//                    .teamRounds(teamRounds)
//                    .build();
//
//            // Add the team to the game's team map
//            ConcurrentHashMap<Integer, TeamComponent> teams = new ConcurrentHashMap<>();
//            teams.put(1, team);
//
//            // Create a mock game component
//            GameComponent game = GameComponent.builder()
//                    .gameId(123)
//                    .roomName("Mock Room")
//                    .themeId(1)
//                    .leaderId(1)
//                    .roundCount(3)
//                    .stage1Time(30)
//                    .stage2Time(15)
//                    .roomCreateTime(LocalDateTime.now().minusHours(1))
//                    .teams(teams)
//                    .questions(List.of(/* Add QuestionComponent objects here if needed */))
//                    .status(GameStatus.READY)
//                    .build();
//
//            return game;
//        }
//    }
//}