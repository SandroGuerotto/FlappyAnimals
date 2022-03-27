package flappyanimal.core;

import flappyanimal.core.model.Character;
import flappyanimal.core.model.Player;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.Map;

/**
 * Holds the complete game logic.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class Game {
    /**
     * Defines all possible game states
     */
    public enum GameState {
        INITIALIZED, RUNNING, PAUSED, FINISHED
    }

    private static final String OBSTACLE_IMAGE = "/core/rock.png";

    /**
     * Selected character
     */
    private final ObjectProperty<Character> character = new SimpleObjectProperty<>();
    /**
     * Current game state
     */
    private final ObjectProperty<GameState> state = new SimpleObjectProperty<>();
    /**
     * Map of all characters with their unique IDs
     */
    private final Map<Integer, Character> allCharacters;

    private final Player player;
    private GameEngine gameEngine;
    private Pane board;
    private ObstacleSpawner obstacleSpawner;

    /**
     * Creates a new game object.
     *
     * @param charData   a list of all characters
     * @param playerData all data from a player. Contains balance, highscore etc.
     */
    public Game(Map<Integer, Character> charData, Player playerData) {
        this.allCharacters = charData;
        this.player = playerData;
    }

    /**
     * Initializes the game. Resets:
     * <ul>
     *     <li>the current character (player model)</li>
     *     <li>player data</li>
     *     <li>game engine</li>
     *     <li>obstacle spawner</li>
     * </ul>
     * After everything is initialized the {@link #state} gets change to {@link GameState#INITIALIZED}.
     */
    public void init() {
        character.setValue(initCharacter());
      
        obstacleSpawner = ObstacleSpawner.create(board, OBSTACLE_IMAGE);
        gameEngine = GameEngine.create(this, obstacleSpawner.getObstacles());
        player.clearCurrentScore();

        state.setValue(GameState.INITIALIZED);
    }

    /**
     * Getter for the game state property.
     *
     * @return game state property
     */
    public ObjectProperty<GameState> stateProperty() {
        return state;
    }


    /**
     * Gets active character id from the player and loads the hitbox.
     *
     * @return fully setup character
     */
    private Character initCharacter() {
        Character currChar = allCharacters.get(player.getCurrentChar());
        currChar.buildHitbox();
        return currChar;
    }

    /**
     * Pauses the game. This includes pausing the obstacle spawner, game engine and show the menu.
     * Sets game state to {@link GameState#PAUSED}.
     */
    public void pause() {
        if (state.getValue() != GameState.RUNNING && state.getValue() != GameState.INITIALIZED)
            throw new IllegalStateException();

        state.setValue(GameState.PAUSED);
        gameEngine.pause();
        obstacleSpawner.pause();
    }

    /**
     * When game state is in {@link GameState#RUNNING}, it invokes jump from the character.
     */
    public void jump() {
        if (state.getValue() != GameState.RUNNING)
            throw new IllegalStateException();
        character.getValue().jump();
    }

    /**
     * Getter for the player instance
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Starts the game. Sets game state to {@link GameState#RUNNING}.
     * All spawners and engines are now running(playing).
     */
    public void runGame() {
        if (state.getValue() != GameState.INITIALIZED && state.getValue() != GameState.PAUSED) {
            throw new IllegalStateException();
        }
        state.setValue(GameState.RUNNING);
        gameEngine.play();
        obstacleSpawner.play();
    }

    /**
     * Setter for GUI to set the floor boundaries.
     *
     * @param floor shape that acts as floor
     */
    public void registerFloor(Shape floor) {
        gameEngine.setFloor(floor);
    }

    /**
     * Setter for GUI to set the game board/canvas.
     *
     * @param board Pane that acts as game board
     */
    public void registerBoard(Pane board) {
        this.board = board;
    }

    /**
     * Sets game in finished state and stops all engines.
     */
    public void gameOver() {
        gameEngine.stop();
        obstacleSpawner.stop();
        player.setBalance(player.getBalance() + player.getCurrentScore());
        state.setValue(GameState.FINISHED);
    }

    /**
     * Resets the game by destroying all obstacles.
     */
    public void restart() {
        obstacleSpawner.destroyObstacles(true);
        init();
    }

    /**
     * Getter for the character property.
     *
     * @return character property
     */
    public ObjectProperty<Character> characterProperty() {
        return character;
    }
}