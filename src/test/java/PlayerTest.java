import flappyanimal.core.model.Player;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static flappyanimal.core.model.Player.MAX_BALANCE;
import static flappyanimal.core.model.Player.MAX_HIGHSCORES;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    private Player cut;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cut = new Player(50, FXCollections.observableArrayList(), FXCollections.observableArrayList(1), 0);
    }

    @Test
    void testEmptyHighscore() {
        cut.addHighscore("Test", 5);

        assertEquals("Test", cut.getHighscores().get(0).getName());
        assertEquals(5, cut.getHighscores().get(0).getScore());
    }

    @Test
    void testPartialHighscore() {
        cut.addHighscore("Existing", 8);

        cut.addHighscore("Test", 5);

        assertEquals("Test", cut.getHighscores().get(1).getName());
        assertEquals(5, cut.getHighscores().get(1).getScore());
    }

    @Test
    void testFullHighscore() {
        cut.addHighscore("Existing1", 10);
        cut.addHighscore("Existing2", 8);
        cut.addHighscore("Existing3", 5);

        cut.addHighscore("Test", 9);

        assertEquals(MAX_HIGHSCORES, cut.getHighscores().size());
        assertEquals("Test", cut.getHighscores().get(1).getName());
        assertEquals(9, cut.getHighscores().get(1).getScore());

        assertEquals("Existing2", cut.getHighscores().get(2).getName());
        assertEquals(8, cut.getHighscores().get(2).getScore());
    }

    @Test
    void testFullHighscoreToLow() {
        cut.addHighscore("Existing1", 10);
        cut.addHighscore("Existing2", 8);
        cut.addHighscore("Existing3", 5);

        cut.addHighscore("Test", 4);

        assertEquals(MAX_HIGHSCORES, cut.getHighscores().size());
        assertEquals("Existing3", cut.getHighscores().get(2).getName());
        assertEquals(5, cut.getHighscores().get(2).getScore());
    }

    @Test
    void testAddBalance() {
        cut.setBalance(500);

        assertEquals(500, cut.getBalance());
    }

    @Test
    void testAddBalanceMaximum() {
        cut.setBalance(MAX_BALANCE + 5);

        assertEquals(MAX_BALANCE, cut.getBalance());
    }

    @Test
    void testAddPoint() {
        cut.currentScoreProperty().set(5);
        cut.addPoint();

        assertEquals(6, cut.getCurrentScore());
    }
}
