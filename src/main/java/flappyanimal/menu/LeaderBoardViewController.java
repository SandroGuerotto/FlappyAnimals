package flappyanimal.menu;

import flappyanimal.core.Game;
import flappyanimal.core.Screens;
import flappyanimal.core.model.Player;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Leaderboard.
 * Contains everything the controller has to reach in the menu (controls) and all methods the menu view calls based on events.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class LeaderBoardViewController {

    private static final int MAX_PLAYER_NAME = 10;
    @FXML
    private Label lbl_firstScore;
    @FXML
    private Label lbl_secondScore;
    @FXML
    private Label lbl_thirdScore;

    @FXML
    private TextField enterYourNameTextfield;
    @FXML
    private Label yourScore;

    @FXML
    private AnchorPane pane;

    private final Game game;
    private final Map<Screens, Pane> screens;
    private List<Label> scoreLabelList;

    /**
     * Create leaderboard controller
     *
     * @param game    game logic
     * @param screens all screens
     */
    public LeaderBoardViewController(Game game, Map<Screens, Pane> screens) {
        this.game = game;
        this.screens = screens;
    }

    /**
     * The labels for the winners' podium get filled in a list. The achieved score of the player will be set on the scroll.
     * Listener to the highscorelist and to the current score is added, to update the winner's podium respectively the achieved score on the scroll.
     */
    @FXML
    public void initialize() {
        game.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Game.GameState.FINISHED) {
                enterYourNameTextfield.setDisable(false);
                achievedScore();
            }
        });
        scoreLabelList = Arrays.asList(lbl_firstScore, lbl_secondScore, lbl_thirdScore);

        updateScore(game.getPlayer().getHighscores());

        game.getPlayer().getHighscores().addListener((ListChangeListener<Player.HighScore<String, Integer>>) c -> updateScore(c.getList()));
        game.getPlayer().currentScoreProperty().addListener((observable, oldValue, newValue) -> achievedScore());
    }

    /**
     * Sets the current achieved score of the player on the scroll.
     */
    private void achievedScore() {
        yourScore.setText(String.valueOf(game.getPlayer().getCurrentScore()));
    }

    /**
     * After the enter key gets pressed, the name of the player with his achieved score will be added to the players highscorelist.
     * Afterwards the textfield for the players name gets disabled. To limit the name length, only the first ten letters will be saved.
     */
    @FXML
    private void setPlayerName() {
        enterYourNameTextfield.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                enterYourNameTextfield.setDisable(true);
            }
        });
        String name = enterYourNameTextfield.getText().substring(0, Math.min(MAX_PLAYER_NAME, enterYourNameTextfield.getText().length()));
        game.getPlayer().addHighscore(name, game.getPlayer().getCurrentScore());
    }

    /**
     * Textfield for the players' name gets cleared, and the main screen will be shown.
     */
    @FXML
    private void leaveLeaderboard() {
        enterYourNameTextfield.clear();
        enterYourNameTextfield.getScene().setRoot(screens.get(Screens.MAIN));
    }

    /**
     * The labels for the scores podium get updated.
     *
     * @param list observablelist with highscores from player class
     */
    private void updateScore(ObservableList<? extends Player.HighScore<String, Integer>> list) {
        for (int i = 0; i < scoreLabelList.size(); i++) {
            if (list.size() > i) {
                scoreLabelList.get(i).setText(list.get(i).toString());
            }
        }
    }
}

