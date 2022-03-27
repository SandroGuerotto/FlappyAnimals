import flappyanimal.core.Game;
import flappyanimal.core.GameEngine;
import flappyanimal.core.model.Character;
import flappyanimal.core.model.Entity;
import flappyanimal.core.model.Player;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameEngineTest {
    private GameEngine cut;
    @Mock
    private Game gameMock;


    private List<Entity> entity = new ArrayList<>();
    @Mock
    private Entity obstacleMock;
    @Mock
    private SimpleObjectProperty<Character> charPropMock;
    @Mock
    private Character charMock;

    @Mock
    private Rectangle floorMock;
    @Mock
    private Polygon charHitbox;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        entity.add(obstacleMock);
        cut = GameEngine.create(gameMock, entity);
        cut.setFloor(floorMock);
    }

    /**
     * Tests a game tick.
     * No collision and no passed obstacles.
     */
    @Test
    void testTick() {
        Bounds fBMock = Mockito.mock(Bounds.class); // floor bounds
        Bounds hbMock = Mockito.mock(Bounds.class); // hitbox bounds
        Bounds oBMock = Mockito.mock(Bounds.class); // obstacle bounds
        Polygon oHMock = mock(Polygon.class); // obstacle hitbox

        //configure floor and ceiling check
        when(gameMock.characterProperty()).thenReturn(charPropMock);
        when(charPropMock.getValue()).thenReturn(charMock);
        when(charMock.getHitbox()).thenReturn(charHitbox);
        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);
        when(hbMock.getMaxY()).thenReturn(50d);
        when(hbMock.getMinY()).thenReturn(45d);

        when(floorMock.getBoundsInLocal()).thenReturn(fBMock);
        when(fBMock.getMinY()).thenReturn(70d);

        //configure obstacle check
        when(obstacleMock.getHitbox()).thenReturn(oHMock);
        when(oHMock.getBoundsInParent()).thenReturn(oBMock);
        when(oBMock.intersects(any())).thenReturn(false);
        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);

        //configure pass check
        when(obstacleMock.getBounds()).thenReturn(oBMock);
        when(oBMock.getCenterX()).thenReturn(50d);

        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);
        when(hbMock.getCenterX()).thenReturn(48d);

        cut.gameTick();

        verify(gameMock).characterProperty();
        verify(charPropMock).getValue();
        verify(obstacleMock).tick();
        verify(charMock, times(2)).getHitbox();
    }

    @Test
    void testCollisionCeiling() {
        Bounds fBMock = Mockito.mock(Bounds.class); // floor bounds
        Bounds hbMock = Mockito.mock(Bounds.class); // hitbox bounds

        //configure floor and ceiling check
        when(gameMock.characterProperty()).thenReturn(charPropMock);
        when(charPropMock.getValue()).thenReturn(charMock);
        when(charMock.getHitbox()).thenReturn(charHitbox);
        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);
        when(hbMock.getMaxY()).thenReturn(50d);
        when(hbMock.getMinY()).thenReturn(0d);

        when(floorMock.getBoundsInLocal()).thenReturn(fBMock);
        when(fBMock.getMinY()).thenReturn(70d);

        cut.checkCollisions(charHitbox);
        verify(gameMock).gameOver();
    }

    @Test
    void testCollisionFloor() {
        Bounds fBMock = Mockito.mock(Bounds.class); // floor bounds
        Bounds hbMock = Mockito.mock(Bounds.class); // hitbox bounds

        //configure floor and ceiling check
        when(gameMock.characterProperty()).thenReturn(charPropMock);
        when(charPropMock.getValue()).thenReturn(charMock);
        when(charMock.getHitbox()).thenReturn(charHitbox);
        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);
        when(hbMock.getMaxY()).thenReturn(70d);
        when(hbMock.getMinY()).thenReturn(65d);

        when(floorMock.getBoundsInLocal()).thenReturn(fBMock);
        when(fBMock.getMinY()).thenReturn(70d);

        cut.checkCollisions(charHitbox);
        verify(gameMock).gameOver();
    }

    @Test
    void testCollisionNearMiss() {
        Bounds fBMock = Mockito.mock(Bounds.class); // floor bounds
        Bounds hbMock = Mockito.mock(Bounds.class); // hitbox bounds
        Bounds oBMock = Mockito.mock(Bounds.class); // obstacle bounds
        Polygon oHMock = mock(Polygon.class); // obstacle hitbox

        //configure floor and ceiling check
        when(gameMock.characterProperty()).thenReturn(charPropMock);
        when(charPropMock.getValue()).thenReturn(charMock);
        when(charMock.getHitbox()).thenReturn(charHitbox);
        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);
        when(hbMock.getMaxY()).thenReturn(50d);
        when(hbMock.getMinY()).thenReturn(45d);

        when(floorMock.getBoundsInLocal()).thenReturn(fBMock);
        when(fBMock.getMinY()).thenReturn(70d);

        //configure obstacle check
        when(obstacleMock.getHitbox()).thenReturn(oHMock);
        when(oHMock.getBoundsInParent()).thenReturn(oBMock);
        when(oBMock.intersects(any())).thenReturn(true);
        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);

        //configure shape intersects
        MockedStatic<Shape> shapeMockedStatic = Mockito.mockStatic(Shape.class);
        Shape interMock = mock(Shape.class);
        Bounds iBMock = Mockito.mock(Bounds.class); // obstacle bounds
        shapeMockedStatic.when(() -> Shape.intersect(any(), any())).thenReturn(interMock);
        when(interMock.getBoundsInParent()).thenReturn(iBMock);
        when(iBMock.getWidth()).thenReturn(-1d);
        when(iBMock.getHeight()).thenReturn(-1d);

        cut.checkCollisions(charHitbox);
        verify(gameMock, never()).gameOver();
        shapeMockedStatic.close();
    }

    @Test
    void testCollisionHit() {
        Bounds fBMock = Mockito.mock(Bounds.class); // floor bounds
        Bounds hbMock = Mockito.mock(Bounds.class); // hitbox bounds
        Bounds oBMock = Mockito.mock(Bounds.class); // obstacle bounds
        Polygon oHMock = mock(Polygon.class); // obstacle hitbox

        //configure floor and ceiling check
        when(gameMock.characterProperty()).thenReturn(charPropMock);
        when(charPropMock.getValue()).thenReturn(charMock);
        when(charMock.getHitbox()).thenReturn(charHitbox);
        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);
        when(hbMock.getMaxY()).thenReturn(50d);
        when(hbMock.getMinY()).thenReturn(45d);

        when(floorMock.getBoundsInLocal()).thenReturn(fBMock);
        when(fBMock.getMinY()).thenReturn(70d);

        //configure obstacle check
        when(obstacleMock.getHitbox()).thenReturn(oHMock);
        when(oHMock.getBoundsInParent()).thenReturn(oBMock);
        when(oBMock.intersects(any())).thenReturn(true);
        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);

        //configure shape intersects
        MockedStatic<Shape> shapeMockedStatic = Mockito.mockStatic(Shape.class);
        Shape interMock = mock(Shape.class);
        Bounds iBMock = Mockito.mock(Bounds.class); // obstacle bounds
        shapeMockedStatic.when(() -> Shape.intersect(any(), any())).thenReturn(interMock);
        when(interMock.getBoundsInParent()).thenReturn(iBMock);
        when(iBMock.getWidth()).thenReturn(0d);
        when(iBMock.getHeight()).thenReturn(1d);

        cut.checkCollisions(charHitbox);
        verify(gameMock).gameOver();
        shapeMockedStatic.close();
    }

    @Test
    void testPassedObstacle() {
        Bounds oBMock = Mockito.mock(Bounds.class); // obstacle bounds
        Bounds hbMock = Mockito.mock(Bounds.class); //hitbox bounds
        Player playerMock = Mockito.mock(Player.class);

        when(obstacleMock.getBounds()).thenReturn(oBMock);
        when(oBMock.getCenterX()).thenReturn(50d);

        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);
        when(hbMock.getCenterX()).thenReturn(49d);

        when(gameMock.getPlayer()).thenReturn(playerMock);

        cut.passedObstacle(charHitbox);

        verify(gameMock).getPlayer();
        verify(playerMock).addPoint();
    }

    @Test
    void testMissedObstacle() {
        Bounds oBMock = Mockito.mock(Bounds.class); // obstacle bounds
        Bounds hbMock = Mockito.mock(Bounds.class); //hitbox bounds
        Player playerMock = Mockito.mock(Player.class);

        when(obstacleMock.getBounds()).thenReturn(oBMock);
        when(oBMock.getCenterX()).thenReturn(50d);

        when(charHitbox.getBoundsInParent()).thenReturn(hbMock);
        when(hbMock.getCenterX()).thenReturn(48d);

        cut.passedObstacle(charHitbox);

        verify(gameMock, never()).getPlayer();
        verify(playerMock, never()).addPoint();
    }
}
