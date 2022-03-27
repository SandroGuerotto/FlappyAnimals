import flappyanimal.core.Game;
import flappyanimal.core.GameEngine;
import flappyanimal.core.ObstacleSpawner;
import flappyanimal.core.model.Character;
import flappyanimal.core.model.Player;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameTest {

    private Game cut; //code under test: Game.java

    @Mock
    private Character charMock;
    @Mock
    private Player playerMock;

    @Mock
    private MockedStatic<ObstacleSpawner> obstacleFactoryMock;
    @Mock
    private ObstacleSpawner spawnerMock;

    @Mock
    private MockedStatic<GameEngine> engineFactoryMock;
    @Mock
    private GameEngine engineMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cut = new Game(Collections.singletonMap(1, charMock), playerMock);
    }

    @AfterEach
    void tearDown() {
        obstacleFactoryMock.close();
        engineFactoryMock.close();
    }

    @Test
    void testInitializeGame() {
        SimpleIntegerProperty scoreMock = mock(SimpleIntegerProperty.class);
      
        when(playerMock.getCurrentChar()).thenReturn(1);
        obstacleFactoryMock.when(() -> ObstacleSpawner.create(any(), any())).thenReturn(spawnerMock);
        engineFactoryMock.when(() -> GameEngine.create(any(), any())).thenReturn(engineMock);

        when(playerMock.currentScoreProperty()).thenReturn(scoreMock);

        cut.init();

        assertEquals(Game.GameState.INITIALIZED, cut.stateProperty().getValue());
        assertEquals(charMock, cut.characterProperty().getValue());
        obstacleFactoryMock.verify(() -> ObstacleSpawner.create(any(), any()));
        engineFactoryMock.verify(() -> GameEngine.create(any(), any()));
    }

    @Test
    void testJumpRunning() {
        testInitializeGame();

        cut.stateProperty().setValue(Game.GameState.RUNNING);

        cut.jump();

        verify(charMock).jump();
    }

    @Test
    void testJumpPaused() {
        testInitializeGame();

        cut.stateProperty().setValue(Game.GameState.PAUSED);

        assertThrows(IllegalStateException.class, () -> cut.jump());

        verify(charMock, never()).jump();
    }

    @Test
    void testGetPlayer() {
        testInitializeGame();

        Player act = cut.getPlayer();
        assertEquals(playerMock, act);
    }

    @Test
    void testPauseValid() {
        testInitializeGame();

        cut.stateProperty().setValue(Game.GameState.RUNNING);

        cut.pause();

        assertEquals(Game.GameState.PAUSED, cut.stateProperty().getValue());
        verify(spawnerMock).pause();
        verify(engineMock).pause();
    }

    @Test
    void testPauseInvalid() {
        testInitializeGame();

        cut.stateProperty().setValue(Game.GameState.FINISHED);

        assertThrows(IllegalStateException.class, () -> cut.pause());

        assertEquals(Game.GameState.FINISHED, cut.stateProperty().getValue());
    }

    @Test
    void testRunGameValid() {
        testInitializeGame();

        // test in game state INITIALIZED
        cut.runGame();

        assertEquals(Game.GameState.RUNNING, cut.stateProperty().getValue());

        // test in game state PAUSED
        cut.stateProperty().setValue(Game.GameState.PAUSED);

        cut.runGame();

        assertEquals(Game.GameState.RUNNING, cut.stateProperty().getValue());
        verify(spawnerMock, times(2)).play();
        verify(engineMock, times(2)).play();
    }

    @Test
    void testRunGameInvalid() {
        testInitializeGame();
        // test in game state FINISHED
        cut.stateProperty().setValue(Game.GameState.FINISHED);

        assertThrows(IllegalStateException.class, () -> cut.runGame());

        assertEquals(Game.GameState.FINISHED, cut.stateProperty().getValue());

        // test in game state RUNNING
        cut.stateProperty().setValue(Game.GameState.RUNNING);

        assertThrows(IllegalStateException.class, () -> cut.runGame());

        assertEquals(Game.GameState.RUNNING, cut.stateProperty().getValue());
        verify(spawnerMock, never()).play();
        verify(engineMock, never()).play();
    }

    @Test
    void testRegisterFloor() {
        testInitializeGame();

        cut.registerFloor(any());

        verify(engineMock).setFloor(any());
    }

    @Test
    void testGameOver() {
        testInitializeGame();
        when(playerMock.getCurrentScore()).thenReturn(500);
        when(playerMock.getBalance()).thenReturn(500);

        cut.gameOver();

        assertEquals(Game.GameState.FINISHED, cut.stateProperty().getValue());
        verify(playerMock).setBalance(1000);
        verify(engineMock).stop();
        verify(spawnerMock).stop();
    }

    @Test
    void testRestart() {
        testInitializeGame();

        when(playerMock.getCurrentChar()).thenReturn(1);

        cut.restart();

        assertEquals(charMock, cut.characterProperty().getValue());
        assertEquals(Game.GameState.INITIALIZED, cut.stateProperty().getValue());

        obstacleFactoryMock.verify(() -> ObstacleSpawner.create(any(), any()), times(2));
        engineFactoryMock.verify(() -> GameEngine.create(any(), any()), times(2));

        verify(engineMock,never()).play();
        verify(spawnerMock,never()).play();
        verify(spawnerMock).destroyObstacles(true);
    }

}
