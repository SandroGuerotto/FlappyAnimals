package flappyanimal.shop;

import flappyanimal.core.Screens;
import flappyanimal.core.model.Character;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.Map;

/**
 * Controller for the shop view.
 * Contains everything the controller has to reach in the view (controls) and
 * all methods the view calls based on events.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class ShopViewController {


    private final Map<Screens, Pane> screens;
    private final Shop shop;

    @FXML
    private ListView<Character> characterList;

    @FXML
    private Text scorePointText;

    @FXML
    private Button backButton;

    /**
     * Initializes view.
     */
    @FXML
    public void initialize() {
        shop.getPlayer().balanceProperty().addListener((observable, oldValue, newValue) -> {
                    scorePointText.setText(String.valueOf(newValue));
                    setupListView();
                }
        );


        scorePointText.setText(String.valueOf(shop.getPlayer().getBalance()));
        setupListView();
    }

    /**
     * @param shop    shop instance
     * @param screens all screens
     */
    public ShopViewController(Shop shop, Map<Screens, Pane> screens) {
        this.shop = shop;
        this.screens = screens;
    }


    /**
     * Set's up the ListView
     */
    private void setupListView() {
        ObservableList<Character> observableCharacterList = FXCollections.observableList(shop.getCharacters());
        characterList.getItems().clear();
        characterList.setItems(observableCharacterList);
        characterList.setCellFactory(param -> new ShopListItem(shop));
    }

    /**
     * Leaves shop
     */
    @FXML
    private void leaveShop() {
        scorePointText.getScene().setRoot(screens.get(Screens.MAIN));
    }

}
