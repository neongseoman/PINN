//package com.ssafy.be.common.component;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import com.ssafy.be.common.model.dto.SocketDTO;
//import com.ssafy.be.gamer.model.GamerPrincipalVO;
//import com.ssafy.be.lobby.model.vo.ExitRoomVO;
//import java.util.concurrent.ConcurrentHashMap;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//class GameManagerTest {
//
//    // 테스트할 객체 생성함
//    private GameManager gameManager;
//
//    @Before("")
//    public void setUp() {
//        gameManager = new GameManager();
//    }
//
//    @Test
//    public void testExitRoomAsLeader() {
//        // Given
//        SocketDTO socketDTO = new SocketDTO("록바이슨", 1, 1);
//        GameComponent gameComponent = mock(GameComponent.class);
//
//        TeamComponent teamComponent = mock(TeamComponent.class);
//        TeamGamerComponent teamGamerComponent = new TeamGamerComponent();
//
//        when(gameComponent.getLeaderId()).thenReturn(gamerPrincipalVO.getGamerId());
//        when(gameComponent.getTeams()).thenReturn(new ConcurrentHashMap<>());
//        when(gameComponent.teams.get(anyInt())).thenReturn(teamComponent);
//        when(teamComponent.getTeamGamers()).thenReturn(new ConcurrentHashMap<>());
//        when(teamComponent.getTeamGamers().get(anyInt())).thenReturn(teamGamerComponent);
//
//        // When
//        ExitRoomVO exitRoomVO = gameManager.exitRoom(socketDTO, gamerPrincipalVO);
//
//        // Then
//        assertNotNull(exitRoomVO);
//        assertTrue(exitRoomVO.isChangeLeader());
//        // Add more assertions as needed
//    }
//
//    @Test
//    public void testExitRoomAsNonLeader() {
//        // Given
//        SocketDTO socketDTO = new SocketDTO(...);
//        GamerPrincipalVO gamerPrincipalVO = new GamerPrincipalVO(...);
//        GameComponent gameComponent = mock(GameComponent.class);
//        TeamComponent teamComponent = mock(TeamComponent.class);
//        TeamGamerComponent teamGamerComponent = new TeamGamerComponent(...);
//        when(gameComponent.getLeaderId()).thenReturn(gamerPrincipalVO.getGamerId() + 1); // Non-leader ID
//        when(gameComponent.getTeams()).thenReturn(new ConcurrentHashMap<>());
//        when(gameComponent.teams.get(anyInt())).thenReturn(teamComponent);
//        when(teamComponent.getTeamGamers()).thenReturn(new ConcurrentHashMap<>());
//        when(teamComponent.getTeamGamers().get(anyInt())).thenReturn(teamGamerComponent);
//
//        // When
//        ExitRoomVO exitRoomVO = gameManager.exitRoom(socketDTO, gamerPrincipalVO);
//
//        // Then
//        assertNotNull(exitRoomVO);
//        assertFalse(exitRoomVO.isChangeLeader());
//        // Add more assertions as needed
//    }
//}
//}