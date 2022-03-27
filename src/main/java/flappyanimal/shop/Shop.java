package flappyanimal.shop;

import flappyanimal.core.model.Player;
import flappyanimal.core.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Holds the complete logic for the shop.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class Shop {
    private final Map<Integer, Character> charData;
    private final Player playerData;

    /**
     * Creates a shop object
     * @param charData all characters available
     * @param playerData player data
     */
    public Shop(Map<Integer, Character> charData, Player playerData) {
        this.charData = charData;
        this.playerData = playerData;
    }

    /**
     * Checks if the player has enough balance to buy a character and buys the character if so
     *
     * @param character to know which character to buy
     */
    public void buyCharacter(Character character) throws BalanceToLowException {
        int balanceOfPlayer = playerData.getBalance();
        if (balanceOfPlayer >= character.getCosts()) {
            playerData.setBalance(Math.subtractExact(balanceOfPlayer, character.getCosts()));
            playerData.getUnlockedChars().add(character.getId());
        } else {
            throw new BalanceToLowException("Balance: " + balanceOfPlayer + " Costs: " + character.getCosts());
        }
    }


    /**
     * sets a new character as current character
     *
     * @param charId which will be the new current character
     */
    public void selectCharacter(int charId) {
        playerData.setCurrentChar(charId);
    }

    /**
     * getter for player data
     *
     * @return player
     */
    public Player getPlayer() {
        return playerData;
    }

    /**
     * getter for the characters
     *
     * @return List of characters
     */
    public List<Character> getCharacters() {
        return new ArrayList<>(charData.values());
    }
}
