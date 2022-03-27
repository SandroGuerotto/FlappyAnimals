package flappyanimal.core;

import flappyanimal.core.model.Entity;
import flappyanimal.core.model.Character;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.List;
/**
 * Game engine handles everything that happens during the game.
 * Game engine checks for collisions and if the character passes an Obstacles.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class GameEngine {

    private static final int GAME_ENGINE_SPEED = 20;
    private static final double PASSED_DEVIATION = 1.5d;

    private final Game game;
    private final List<Entity> obstacles;
    private final Timeline engine;
    private Shape floor;

    /**
     * Factory method to create a new {@link GameEngine}.
     *
     * @param game      game logic
     * @param obstacles obstacle spawner that create obstacles
     * @return game engine
     */
    public static GameEngine create(Game game, List<Entity> obstacles) {
        return new GameEngine(game, obstacles);
    }

    /**
     * Create a new timeline to act as a game engine.
     * Every {@link #GAME_ENGINE_SPEED}ms a game tick happens.
     * A tick invokes {@link Entity#tick()} of all obstacles and the character.
     * After every entity on the board moved, a collision check is made and
     * if an obstacle has passed the character to add a point.
     */
    private GameEngine(Game game, List<Entity> obstacles) {
        this.game = game;
        this.obstacles = obstacles;

        this.engine = new Timeline(new KeyFrame(Duration.millis(GAME_ENGINE_SPEED),
                (event) -> gameTick()));
        this.engine.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Moves character and obstacles invokes collision pass detection.
     */
    public void gameTick() {
        Character character = game.characterProperty().getValue();
        character.tick();
        obstacles.forEach(Entity::tick);
        checkCollisions(character.getHitbox());
        passedObstacle(character.getHitbox());
    }

    /**
     * Checks if the character hitbox hits the floor, ceiling or an obstacle.
     * <ul>
     *     <li>Floor: check is done by comparing the Y-Axis of the floor to the maxY of the character</li>
     *     <li>Ceiling: check is done by comparing the minY of the character, not smaller than 0 </li>
     *     <li>Obstacle: first check is done by comparing their bounds in parent(Rectangle).
     *     If a potential hit is registered, an additional check with Shape.intersect() is done.
     *     Because Shape.intersect() is an expensive operation within multiple milliseconds, the previous check is necessary</li>
     * </ul>
     *
     * @param hitbox character hitbox
     */
    public void checkCollisions(Polygon hitbox) {
        if (floor.getBoundsInLocal().getMinY() <= hitbox.getBoundsInParent().getMaxY()
                || hitbox.getBoundsInParent().getMinY() <= 0) {
            game.gameOver();
            return;
        }
        for (Entity obstacle : obstacles) {
            if (obstacle.getHitbox().getBoundsInParent().intersects(hitbox.getBoundsInParent())) {
                Shape hit = Shape.intersect(hitbox, obstacle.getHitbox());
                if (hit.getBoundsInParent().getWidth() != -1 || hit.getBoundsInParent().getHeight() != -1) {
                    game.gameOver();
                }
            }
        }
    }

    /**
     * If the character hitbox passes successfully an obstacle add one point to the current score.
     * Checks if the difference of centerX of one obstacle and centerX of the hitbox is smaller than 1.
     *
     * @param hitbox character hitbox
     */
    public void passedObstacle(Shape hitbox) {
        for (Entity obstacle : obstacles) {
            if (Math.abs(obstacle.getBounds().getCenterX() - hitbox.getBoundsInParent().getCenterX()) <= PASSED_DEVIATION) {
                game.getPlayer().addPoint();
                return;
            }
        }
    }

    /**
     * Wrapper to start the spawning of new obstacles.
     * Uses {@link Timeline#play()}.
     */
    public void play() {
        engine.play();
    }

    /**
     * Wrapper to pause the spawning of new obstacles.
     * Uses {@link Timeline#pause()}.
     */
    public void pause() {
        engine.pause();
    }

    /**
     * Wrapper to stop the spawning of new obstacles.
     * Uses {@link Timeline#stop()}.
     */
    public void stop() {
        engine.stop();
    }

    /**
     * Setter for GUI to set the floor boundaries.
     * Used to check collisions between floor and moving character.
     *
     * @param floor shape that acts as floor
     */
    public void setFloor(Shape floor) {
        this.floor = floor;
    }
}
