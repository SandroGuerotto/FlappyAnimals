package flappyanimal.core;

import flappyanimal.core.model.Character;
import flappyanimal.core.model.Player;
import flappyanimal.menu.LeaderBoardViewController;
import flappyanimal.menu.MenuViewController;
import flappyanimal.shop.Shop;
import flappyanimal.shop.ShopViewController;
import flappyanimal.utils.JSONWriterReader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main entry. Makes initial setups:
 * Preloads all screens.
 * Instead of loading everything within the game login in {@link Game}, everything gets loaded here.
 * In this case it is possible to change the input source independent of the game logic itself.
 * Loads all JSON data required to start the game.
 * Saves all JSON data back to the file on a close request.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class Main extends Application {
    Game game;
    Map<Screens, Pane> screens;
    private Map<Integer, Character> charData;
    private Player playerData;
    private Shop shop;

    /**
     * Main start method
     *
     * @param args start arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the application
     *
     * @param primaryStage active stage
     */
    @Override
    public void start(Stage primaryStage) {
        setupStage(primaryStage);
        setupGameState();

        primaryStage.setScene(new Scene(screens.get(Screens.MAIN)));
        primaryStage.show();
    }

    /**
     * Loads all data and creates a new game object.
     * Loads all views.
     */
    private void setupGameState() {
        charData = loadCharData();
        playerData = loadPlayerData();
        game = new Game(charData, playerData);
        shop = new Shop(charData, playerData);
        screens = loadViews();

        game.init();
    }

    /**
     * Loads player data from JSON file
     *
     * @return player
     */
    private Player loadPlayerData() {
        try {
            return new JSONWriterReader().readFromJson("resources/player.json", Player.class);
        } catch (Exception e) {
            return new Player(0, FXCollections.observableArrayList(), FXCollections.observableArrayList(1), 1);
        }
    }

    /**
     * Loads character data from JSON file
     *
     * @return Map of all characters
     */
    private Map<Integer, Character> loadCharData() {
        try {
            List<Character> characters = new JSONWriterReader().readListFromJson("resources/characters.json", Character.class);
            return characters.stream().collect(Collectors.toMap(Character::getId, c -> c));
        } catch (IOException e) {
            return Collections.singletonMap(1, new Character(3, 70, 5, 1, "Blue bird", new File("images/characters/bird.png")));
        }
    }

    /**
     * Sets properties for the stage.
     *
     * @param stage active stage
     */
    private void setupStage(Stage stage) {
        stage.setResizable(false);
        stage.setTitle("Flappy Animals");
        stage.setOnCloseRequest(event -> exit(stage));
        stage.getIcons().add(new Image(new File("images/characters/bird.png").toURI().toString()));
    }

    /**
     * Exits application
     */
    public void exit(Stage stage) {
        saveData();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Saves player data
     */
    private void saveData() {
        JSONWriterReader json = new JSONWriterReader();
        json.writeToJson("resources/player.json", playerData);
    }

    /**
     * When application stops, save playe data
     *
     * @throws Exception if something goes wrong
     */
    @Override
    public void stop() throws Exception {
        saveData();
        super.stop();
    }

    /**
     * Loads all needed view and sets their controller.
     *
     * @return map with all loaded screens.
     */
    private Map<Screens, Pane> loadViews() {
        Map<Screens, Pane> screens = new HashMap<>();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/GameView.fxml"));
            loader.setController(new GameViewController(game, screens));
            screens.put(Screens.MAIN, loader.load());

            loader = new FXMLLoader(getClass().getResource("/shop/ShopView.fxml"));
            loader.setController(new ShopViewController(shop, screens));
            screens.put(Screens.SHOP, loader.load());

            loader = new FXMLLoader(getClass().getResource("/menu/MenuView.fxml"));
            loader.setController(new MenuViewController(game, screens));
            screens.put(Screens.MENU, loader.load());

            loader = new FXMLLoader(getClass().getResource("/leaderboard/LeaderBoardView.fxml"));
            loader.setController(new LeaderBoardViewController(game, screens));
            screens.put(Screens.LEADERBOARD, loader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return screens;

    }
}
