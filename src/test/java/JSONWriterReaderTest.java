import flappyanimal.core.model.Character;
import flappyanimal.core.model.Player;
import flappyanimal.utils.JSONWriterReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class JSONWriterReaderTest {

    private JSONWriterReader cut;
    private List<Character> data;
    private Player player;

    @BeforeEach
    void setup() {
        cut = new JSONWriterReader();

        data = Arrays.asList(
                new Character(3, 70, 5, 1, "fake", new File("fake.png")),
                new Character(100, 80, 78, 3, "test", new File("test.png")));
        ObservableList<Player.HighScore<String, Integer>> scores = FXCollections.observableArrayList(Collections.singletonList(new Player.HighScore<>("Test", 500)));
        player = new Player(10, scores, FXCollections.observableArrayList(1, 5), 1);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("src/test/resources/testPlayer.json"));
        Files.deleteIfExists(Path.of("src/test/resources/testChars.json"));
    }

    @Test
    void writePlayerData() {
        player.addPoint();
        assertTrue(cut.writeToJson("src/test/resources/testPlayer.json", player));
    }

    @Test
    void readPlayerData() throws IOException {

        writePlayerData();

        Player act = cut.readFromJson("src/test/resources/testPlayer.json", Player.class);

        assertEquals(player.getBalance(), act.getBalance());
        assertEquals(player.getCurrentChar(), act.getCurrentChar());
        assertEquals(player.getUnlockedChars(), act.getUnlockedChars());
        assertEquals(player.getHighscores(), act.getHighscores());
        assertEquals(0, act.getCurrentScore());
    }

    @Test
    void writeCharactersData() {
        assertTrue(cut.writeToJson("src/test/resources/testChars.json", data));
    }

    @Test
    void readCharactersData() throws IOException {
        writeCharactersData();
        List<Character> act = cut.readListFromJson("src/test/resources/testChars.json", Character.class);

        assertEquals(2, act.size());

        assertTrue(isCharEqual(data.get(0), act.get(0)));
        assertTrue(isCharEqual(data.get(1), act.get(1)));
    }

    private boolean isCharEqual(Character exp, Character act) {
        return exp.getCosts() == act.getCosts()
                && exp.getGravity() == act.getGravity()
                && exp.getId() == act.getId()
                && exp.getStrength() == act.getStrength()
                && exp.getImagePath().equals(act.getImagePath());
    }
}
