package flappyanimal.core;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Map;

/**
 * Controller for the GameView.
 * Contains everything the controller has to reach in the view (controls) and
 * all methods the view calls based on events.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class GameViewController {
    @FXML
    private AnchorPane board;
    @FXML
    private StackPane mainPane;
    @FXML
    private Text scorePointText;
    @FXML
    private Text startText;

    @FXML
    private Rectangle floor;
    @FXML
    private Button menuButton;

    private final Game game;
    private final Map<Screens, Pane> screens;
    private Shape characterHitbox;

    /**
     * Creates game controller
     *
     * @param game    game logic
     * @param screens all screens
     */
    public GameViewController(Game game, Map<Screens, Pane> screens) {
        this.game = game;
        this.screens = screens;
    }

    /**
     * Initializes view.
     */
    @FXML
    public void initialize() {

        board.setOnMouseClicked(event -> start());

        game.stateProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case INITIALIZED -> initView();
                case RUNNING -> running();
                case PAUSED, FINISHED -> showMenu();
            }
        });

        game.registerBoard(board);

        menuButton.setOnMouseClicked(event -> game.pause());
    }

    /**
     * Hides the start text and remove menu nodes.
     */
    private void running() {
        removeMenu();
        startText.setVisible(false);
    }

    /**
     * Deletes all node from the screen that belongs to the menu view.
     */
    private void removeMenu() {
        mainPane.getChildren().removeAll(screens.get(Screens.MENU));
    }

    /**
     * Initializes view from {@link Game.GameState#INITIALIZED }.
     * Removes all nodes from the menu view and registers jump action.
     */
    private void initView() {
        removeMenu();
        registerJump();
        addCharacter(game.characterProperty().getValue().getHitbox());
        registerScore();
        game.registerFloor(floor);

        startText.setVisible(true);
    }

    /**
     * Adds the menu elements to the board to act like an overlay.
     */
    private void showMenu() {
        removeMenu();
        mainPane.getChildren().add(screens.get(Screens.MENU));
        startText.setVisible(false);
    }

    /**
     * Adds a listener to the current score to update current score board
     */
    private void registerScore() {
        scorePointText.setText(String.valueOf(0));
        game.getPlayer().currentScoreProperty().addListener((observable, oldValue, newValue) -> scorePointText.setText(newValue.toString()));
    }

    /**
     * Removes the existing character model from the board.
     * Adds the new character model to the board at the position(x,y) (75,150).
     *
     * @param newModel new character model
     */
    private void addCharacter(Shape newModel) {
        board.getChildren().remove(characterHitbox);
        characterHitbox = newModel;
        characterHitbox.setTranslateY(150);
        characterHitbox.setTranslateX(75);
        characterHitbox.setFocusTraversable(true);
        characterHitbox.requestFocus();
        board.getChildren().add(newModel);
    }

    /**
     * Adds an onKeyPressed listener. On spacebar invokes {@link Game#jump()}.
     */
    private void registerJump() {
        board.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE
                    && game.stateProperty().getValue() == Game.GameState.RUNNING) {
                Platform.runLater(game::jump);
            }
        });
    }

    /**
     * Start the game with {@link Game#runGame()} and remove the start text.
     */
    private void start() {
        if (startText.isVisible()) {
            game.runGame();
            startText.setVisible(false);
        }
    }
}
