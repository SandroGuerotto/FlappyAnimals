import flappyanimal.core.ObstacleSpawner;
import flappyanimal.core.model.Obstacle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ObstacleSpawnerTest {

    private ObstacleSpawner cut;
    @Mock
    private AnchorPane board;
    @Mock
    private MockedStatic<Obstacle> obstacleFactoryMock;
    @Mock
    private Obstacle obstacleMock;

    private ObservableList<Node> children;

    /**
     * Open all mocks and setup board
     */
    @BeforeEach
    void setup() {
        board = Mockito.mock(AnchorPane.class);
        when(board.getWidth()).thenReturn(800d);
        when(board.getHeight()).thenReturn(600d);

        obstacleMock = Mockito.mock(Obstacle.class);
        obstacleFactoryMock = Mockito.mockStatic(Obstacle.class);

        cut = ObstacleSpawner.create(board, "test.abc");

        children = FXCollections.observableArrayList();
    }

    @AfterEach
    void tearDown() {
        obstacleFactoryMock.close();
    }

    /**
     * Tests if the correct amount of obstacles are created.
     * Sadly we can not test if all obstacles are in the correct place, because all obstacles are mocked and not newly created.
     */
    @Test
    void testSpawn() {

        obstacleFactoryMock.when(() -> Obstacle.create(anyString(), anyDouble(), anyDouble(), anyBoolean(), anyDouble())).thenReturn(obstacleMock);
        when(board.getChildren()).thenReturn(children);

        cut.spawnObstacle();

        assertEquals(2, children.size());
        assertEquals(2, cut.getObstacles().size());

        obstacleFactoryMock.verify(() -> Obstacle.create(anyString(), anyDouble(), anyDouble(), anyBoolean(), anyDouble()), times(2));
        verify(board, times(2)).getWidth();
        verify(board, times(1)).getHeight();
    }

    /**
     * Tests if all generated obstacles are getting deleted. Independent of their current x-position
     */
    @Test
    void testDestroyAll() {
        testSpawn();

        when(board.getChildren()).thenReturn(children);

        cut.destroyObstacles(true);

        assertEquals(0, children.size());
        assertEquals(0, cut.getObstacles().size());
    }

    /**
     * Tests if obstacles outside of the board (x-axis <=0) are getting deleted.
     */
    @Test
    void testDestroyOutOfBound() {
        BoundingBox boundOutMock = Mockito.mock(BoundingBox.class);
        BoundingBox boundInMock = Mockito.mock(BoundingBox.class);
        Obstacle obstacleOutMock = Mockito.mock(Obstacle.class);

        obstacleFactoryMock.when(() -> Obstacle.create(anyString(), anyDouble(), anyDouble(), anyBoolean(), anyDouble()))
                .thenReturn(obstacleMock).thenReturn(obstacleOutMock);

        when(board.getChildren()).thenReturn(children);

        when(obstacleMock.getBounds()).thenReturn(boundInMock);
        when(boundInMock.getMaxX()).thenReturn(200d);

        when(obstacleOutMock.getBounds()).thenReturn(boundOutMock);
        when(boundOutMock.getMaxX()).thenReturn(-1d);

        cut.spawnObstacle();

        cut.destroyObstacles(false);

        assertEquals(1, children.size());
        assertEquals(1, cut.getObstacles().size());

        verify(boundInMock, times(1)).getMaxX();
        verify(boundOutMock, times(1)).getMaxX();
    }
}
