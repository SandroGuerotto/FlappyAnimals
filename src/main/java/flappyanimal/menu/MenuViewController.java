package flappyanimal.menu;

import flappyanimal.core.Game;
import flappyanimal.core.Screens;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Controller for the MenuView.
 * Contains everything the controller has to reach in the menu (controls) and all methods the menu view calls based on events.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */

public class MenuViewController {

    @FXML
    private Label gameOverLabel;
    @FXML
    private Label scoreLabel;

    @FXML
    private Button shopButton;
    @FXML
    private Button resumeButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private BorderPane borderPane;

    private final Game game;
    private final Map<Screens, Pane> screens;

    /**
     * Creates menu controller
     *
     * @param game    game logic
     * @param screens all screens
     */
    public MenuViewController(Game game, Map<Screens, Pane> screens) {
        this.game = game;
        this.screens = screens;
    }

    /**
     * Initializes view.
     */
    @FXML
    public void initialize() {
        game.stateProperty().addListener((observable, oldValue, newValue) ->
        {
            switch (newValue) {
                case PAUSED -> {
                    gameOverLabel.setText("Game paused");
                    hideButton(shopButton);
                    showButton(resumeButton);
                    setScoreLabel();
                }
                case FINISHED -> {
                    gameOverLabel.setText("Game over");
                    showButton(shopButton);
                    hideButton(resumeButton);
                    setScoreLabel();
                }
            }
        });

    }

    /**
     * Hides given button
     *
     * @param button to hide
     */
    private void hideButton(Button button) {
        button.setVisible(false);
        button.setManaged(false);
        button.setDisable(true);
    }

    /**
     * Shows hidden button
     *
     * @param button to show
     */
    private void showButton(Button button) {
        button.setVisible(true);
        button.setManaged(true);
        button.setDisable(false);
    }

    /**
     * Closes Application
     */
    @FXML
    private void closeWindow() {
        ((Stage) scoreLabel.getScene().getWindow()).close();
    }

    /**
     * Sets game state to game over and enters the shop.
     */
    @FXML
    private void enterShop() {
        scoreLabel.getScene().setRoot(screens.get(Screens.SHOP));
    }

    /**
     * Restart the game
     */
    @FXML
    private void restartGame() {
        game.restart();
    }

    /**
     * Resumes the game
     */
    @FXML
    private void resumeGame() {
        game.runGame();
    }

    /**
     * Sets current score to label
     */
    @FXML
    private void setScoreLabel() {
        scoreLabel.setText(String.valueOf(game.getPlayer().getCurrentScore()));
    }

    /**
     * Opens leader board
     */
    @FXML
    private void openLeaderBoard() {
        scoreLabel.getScene().setRoot(screens.get(Screens.LEADERBOARD));

    }
}
