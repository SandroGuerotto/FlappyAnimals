package flappyanimal.core;

import flappyanimal.core.model.Entity;
import flappyanimal.core.model.Obstacle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * ObstacleSpawner handles spawning and deleting entities like {@link Obstacle} onto and from the board.
 * Using {@link Timeline} to spawn every {@link #SPAWN_SPEED} seconds a group of entities.
 * A group of entities contains two obstacle, one at the top and one at the bottom of the board.
 * If a entity is outside of the field view or board it gets deleted.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class ObstacleSpawner {
    /**
     * Minimum allowed height for an obstacle
     */
    private static final double MIN_HEIGHT = 0.25d;
    /**
     * Maximum allowed height for an obstacle
     */
    private static final double MAX_HEIGHT = 0.8d;
    /**
     * Spawn rate in seconds
     */
    private static final int SPAWN_SPEED = 4;
    /**
     * Path to image for an obstacle.
     */
    private final String imagePath;
    /**
     * All created and active obstacles
     */
    private final List<Entity> obstacles;
    /**
     * Game board where all obstacles are placed.
     */
    private final Pane board;
    /**
     * Random height modifier
     */
    private final Random heightModifier = new Random();
    /**
     * Timeline to periodically spawn new obstacles. Timeline is executed on the FX Application Thread.
     * So adding and change Nodes on to the board gets handles by the correct thread.
     */
    private final Timeline obstacleSpawner;

    /**
     * Initializes {@link #obstacleSpawner} with given spawn rate {@link #SPAWN_SPEED} in seconds.
     *
     * @param board     active game board (obstacles are added here)
     * @param imagePath path to image for the obstacle
     */
    private ObstacleSpawner(Pane board, String imagePath) {
        this.board = board;
        this.imagePath = imagePath;

        this.obstacles = new ArrayList<>();
        this.obstacleSpawner = new Timeline(new KeyFrame(Duration.ZERO,
                (event) -> {
                    spawnObstacle();
                    destroyObstacles(false);
                }), new KeyFrame(Duration.seconds(SPAWN_SPEED)));

        this.obstacleSpawner.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Factory method to create a new ObstacleSpawner.
     *
     * @param board     active game board (obstacles are added here)
     * @param imagePath path to image for the obstacle
     * @return spawner
     */
    public static ObstacleSpawner create(Pane board, String imagePath) {
        return new ObstacleSpawner(board, imagePath);
    }

    /**
     * Destroys created obstacles based on their current maxX-value.
     *
     * @param destroyAll if true, destroy all obstacles, disregarding their maxX-value
     */
    public void destroyObstacles(boolean destroyAll) {
        List<Entity> toDestroy = obstacles.stream().filter(obstacle -> destroyAll || obstacle.getBounds().getMaxX() <= 0).collect(Collectors.toList());
        board.getChildren().removeAll(toDestroy);
        obstacles.removeAll(toDestroy);
    }

    /**
     * Spawns two new obstacles at the top (y=0) and bottom (y=board height) of the board.
     * A random height modifiers gets created to create height diversity.
     * To prevent impossible jumps, a minimum {@link #MIN_HEIGHT} and maximum {@link #MAX_HEIGHT} is set.
     */
    public void spawnObstacle() {
        double scale = heightModifier.nextDouble() + MIN_HEIGHT;

        boolean scaleRandomizer = heightModifier.nextBoolean();
        double heightBottom = Math.max(MAX_HEIGHT, scaleRandomizer ? scale : Obstacle.MAX_SCALE - scale);
        double heightTop = Obstacle.MAX_SCALE - heightBottom;

        Obstacle bottom = Obstacle.create(imagePath, board.getWidth(), board.getHeight(), false, heightBottom);
        Obstacle top = Obstacle.create(imagePath, board.getWidth(), 0, true, heightTop);

        board.getChildren().addAll(bottom, top);
        obstacles.add(bottom);
        obstacles.add(top);
    }

    /**
     * Wrapper to pause the spawning of new obstacles.
     * Uses {@link Timeline#pause()}.
     */
    public void pause() {
        obstacleSpawner.pause();
    }

    /**
     * Wrapper to start the spawning of new obstacles.
     * Uses {@link Timeline#play()}.
     */
    public void play() {
        obstacleSpawner.play();
    }

    /**
     * Wrapper to stop the spawning of new obstacles.
     * Uses {@link Timeline#stop()}.
     */
    public void stop() {
        obstacleSpawner.stop();
    }

    /**
     * Getter for all spawned and active obstacles
     *
     * @return all spawned and active obstacles
     */
    public List<Entity> getObstacles() {
        return obstacles;
    }
}
