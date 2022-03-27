package flappyanimal.utils;

import com.google.gson.reflect.TypeToken;
import org.hildan.fxgson.FxGson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Class to read or write json files
 * contains method to read a json or to write inside a json
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class JSONWriterReader {

    /**
     * Writes object as a json object. Support Lists and simple Objects.
     * In addition write supports {@link javafx.beans.property.Property}.
     * Example: Write List to json
     * List<Character> characters = new ArrayList<Character>();
     * Character character1 = new Character(1, 1, 5, 1, new File("bird.png"));
     * characters.add(character1);
     * JSONWriterReader.writeToJson("resources/characters/char.json", characters);
     *
     * @param filePath path for the json file
     * @param object   what to save
     * @return true if save was possible
     */
    public boolean writeToJson(String filePath, Object object) {

        try (FileWriter writer = new FileWriter(filePath)) {
            String json = FxGson.coreBuilder()
                    .setPrettyPrinting()
                    .create().toJson(object);

            writer.write(json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Reads a JSON File with an array and converts it to a List.
     * Example: creating a list of {@link ch.zhaw.pm2.flappyanimal.core.model.Character} from a JSON file.
     * <pre>
     * {@code
     *  List<Character> characters = JSONWriterReader.readFromJson("resources/characters/char.json", Character.class);
     * }
     * </pre>
     *
     * @param fileName which json to read? // path to json
     * @param clazz    class to convert json
     * @param <T>      ListItem type
     * @return List of the given type
     * @throws IOException can not read file
     */
    public <T> List<T> readListFromJson(String fileName, Class<T> clazz) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(fileName));
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return FxGson.create().fromJson(reader, typeOfT);

    }

    /**
     * Reads a valid json file and parses it to the given class type.
     *
     * @param path  path to json file
     * @param clazz the class of T
     * @param <T>   type of desired object
     * @return create instance of the read json
     * @throws IOException if file could not be read
     */
    public <T> T readFromJson(String path, Class<T> clazz) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(path));
        return FxGson.create().fromJson(reader, clazz);

    }
}
