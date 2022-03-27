package flappyanimal.core.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.util.Objects;

/**
 * Represents a player for the flappy bird.
 * Contains every important data of the user which is playing.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class Player {

    public static final int MAX_HIGHSCORES = 3;
    public static final int MAX_BALANCE = 999999;
    private final IntegerProperty balance = new SimpleIntegerProperty();
    private final ObservableList<HighScore<String, Integer>> highscores;
    private transient IntegerProperty currentScore = new SimpleIntegerProperty(0);
    private final ObservableList<Integer> unlockedChars;
    private final IntegerProperty currentChar = new SimpleIntegerProperty();

    /**
     * Creates a new player object.
     *
     * @param balance       number of coins
     * @param highscores    observableList of highscores
     * @param unlockedChars list of unlocked characters
     * @param currentChar   the current character
     */
    public Player(int balance, ObservableList<HighScore<String, Integer>> highscores, ObservableList<Integer> unlockedChars, int currentChar) {
        this.highscores = highscores;
        this.balance.set(balance);
        this.unlockedChars = unlockedChars;
        this.currentChar.set(currentChar);
    }

    /**
     * Getter for the balance of the player.
     *
     * @return balance of coins
     */
    public int getBalance() {
        return balance.get();
    }

    /**
     * Getter for balance property
     * @return balance property
     */
    public IntegerProperty balanceProperty() {
        return balance;
    }

    /**
     * Balance is limited to 999'999 coins.
     *
     * @param balance new balance
     */
    public void setBalance(int balance) {
        this.balance.set(Math.min(MAX_BALANCE, balance));
    }

    /**
     * Getter for the highscores a player has achieved.
     *
     * @return observableList of highscores.
     */
    public ObservableList<HighScore<String, Integer>> getHighscores() {
        return highscores;
    }

    public void addHighscore(String name, int newHigh) {
        for (int pos = 0; pos < MAX_HIGHSCORES; pos++) {
            if (highscores.size() <= pos || highscores.get(pos).getScore() < newHigh) {
                highscores.add(pos, new HighScore<>(name, newHigh));
                break;
            }
        }
        if (highscores.size() > MAX_HIGHSCORES) {
            highscores.remove(MAX_HIGHSCORES, highscores.size());
        }
    }

    /**
     * Getter for unlocked characters.
     *
     * @return list of unlocked characters.
     */
    public ObservableList<Integer> getUnlockedChars() {
        return unlockedChars;
    }

    /**
     * Getter for current character.
     *
     * @return int of current character.
     */
    public int getCurrentChar() {
        return currentChar.get();
    }

    public IntegerProperty currentCharProperty() {
        return currentChar;
    }

    /**
     * Setter for current character.
     *
     * @param id number for current character.
     */
    public void setCurrentChar(int id) {
        this.currentChar.set(id);
    }

    /**
     * Getter for current score.
     *
     * @return int current score.
     */
    public int getCurrentScore() {
        if (currentScore == null)
            currentScore = new SimpleIntegerProperty(0);
        return currentScore.get();
    }

    /**
     * Creates a property of the current score. If there is no current score, the value of the current score is set to 0.
     *
     * @return property of the current score.
     */
    public IntegerProperty currentScoreProperty() {
        if (currentScore == null)
            currentScore = new SimpleIntegerProperty(0);
        return currentScore;
    }

    /**
     * Adds a point to the current score.
     */
    public void addPoint() {
        if (currentScore == null)
            currentScore = new SimpleIntegerProperty(0);

        currentScore.set(currentScore.getValue() + 1);
    }

    /**
     * Inner class for the highscores. Holds the name of type K and the score of type K of the player.
     *
     * @param <K> name of the player
     * @param <V> score of the player
     */
    public static class HighScore<K, V> {
        K name;
        V score;

        public HighScore(K name, V score) {
            this.name = name;
            this.score = score;
        }

        public V getScore() {
            return score;
        }

        public K getName() {
            return name;
        }

        @Override
        public String toString() {
            return name + ": " + score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HighScore<?, ?> highScore = (HighScore<?, ?>) o;
            return Objects.equals(name, highScore.name) && Objects.equals(score, highScore.score);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, score);
        }
    }

    public void clearCurrentScore() {
        if (currentScore != null)
            currentScore.set(0);
    }
}
