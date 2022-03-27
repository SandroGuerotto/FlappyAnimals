package flappyanimal.shop;


import flappyanimal.core.model.Character;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * Listitem for shop listview
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class ShopListItem extends ListCell<Character> {

    private final Shop shop;

    /**
     * Create a new listitem
     *
     * @param shop shop logic
     */
    public ShopListItem(Shop shop) {
        super();
        this.shop = shop;
    }

    /**
     * Gets called for each ListView Item.
     * Creates for each Item the picture of the character, name, info of their stats and a button.
     */
    public void updateItem(Character character, boolean empty) {
        if (character == null || empty) {
            setText(null);
            return;
        }

        super.updateItem(character, empty);

        BorderPane wrapper = new BorderPane();

        HBox info = buildInfoPane(character);
        HBox button = buildButton(character);
        button.setAlignment(Pos.CENTER);

        wrapper.setCenter(info);
        wrapper.setRight(button);
        wrapper.setPrefHeight(100);


        wrapper.setMinWidth(700);
        wrapper.setPadding(new Insets(5));
        this.setGraphic(wrapper);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Create a button with different actions based on players balance and unlocked characters.
     * If character is selected, button has no action. Text = "Selected"
     * If character is unlocked and not selected, button invokes {@link Shop#selectCharacter(int)}. Text = "Select"
     * If character is purchasable with enough points, button invokes {@link Shop#buyCharacter(Character)}. Text = "Buy (costs)"
     * If character is purchasable with insufficient points, is disabled. Text = "Buy (costs)"
     *
     * @param character current character
     * @return created button
     */
    private HBox buildButton(Character character) {
        final Button result = new Button();

        result.setAlignment(Pos.CENTER_RIGHT);
        result.getStyleClass().add("button-shop");

        shop.getPlayer().currentCharProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == character.getId()) {
                isSelectedChar(result);
            }
            if (oldValue.intValue() == character.getId()) {
                selectableChar(result, character.getId());
            }

        });
        if (shop.getPlayer().getCurrentChar() == character.getId()) {
            isSelectedChar(result);
        } else if (shop.getPlayer().getUnlockedChars().contains(character.getId())) {
            selectableChar(result, character.getId());
        } else {
            result.setText("Buy " + character.getCosts());
            result.disableProperty().bind(Bindings.createBooleanBinding(() ->
                    shop.getPlayer().balanceProperty().lessThan(character.getCosts()).get()
            ));

            result.setOnAction(e -> {
                try {
                    shop.buyCharacter(character);
                    selectableChar(result, character.getId());
                } catch (BalanceToLowException ignored) { // button is already disabled..
                }
            });
        }

        return new HBox(result);
    }

    /**
     * Sets button text to "Select" and adds onAction to select the character
     *
     * @param result action button for character
     */
    private void selectableChar(Button result, int id) {
        result.setText("Select");
        result.setOnAction(e -> shop.selectCharacter(id));
    }

    /**
     * Sets button text to "Selected"
     *
     * @param result action button for character
     */
    private void isSelectedChar(Button result) {
        result.setText("Selected");
    }

    /**
     * Builds stats info pane
     *
     * @param character current character
     * @return info pane
     */
    private HBox buildInfoPane(Character character) {
        HBox infos = new HBox(25);
        VBox stats = new VBox(20);

        Label name = new Label(character.getName());
        name.setMinWidth(150);
        name.setMaxWidth(150);
        name.getStyleClass().add("shop-item-label");

        ImageView imageView = new ImageView(new Image(new File(character.getImagePath()).toURI().toString(), 75, 0, true, true));
        Label strength = new Label("Strength: " + character.getStrength());
        Label gravity = new Label("Gravity: " + character.getGravity());
        strength.getStyleClass().add("shop-item-label");
        gravity.getStyleClass().add("shop-item-label");

        stats.getChildren().addAll(strength, gravity);
        stats.setAlignment(Pos.CENTER_LEFT);
        stats.setPadding(new Insets(0, 0, 0, 25));

        infos.getChildren().addAll(imageView, name, stats);
        infos.setAlignment(Pos.CENTER_LEFT);
        infos.setPadding(new Insets(0, 0, 0, 10));
        return infos;
    }
}