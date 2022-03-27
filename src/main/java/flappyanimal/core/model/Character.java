package flappyanimal.core.model;

import flappyanimal.core.HitboxBuilder;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * Represents a character for flappy animal
 * Contains every method that a character can do.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class Character implements Entity {

    private static final int MAX_JUMP_ROTATION = -60;
    private static final int JUMP_ROTATION = -50;
    private static final int MAX_FALL_ROTATION = 70;
    private static final int FALL_ROTATION = 2;
    private static final double DEFAULT_SCALE = 0.15;
    private final int gravity;
    private final int strength;
    private final int costs;
    private final int id;
    private final String imagePath;
    private transient Polygon hitbox;
    private final String name;

    /**
     * Creates a character model.
     *
     * @param gravity  gravity strength. determines how fast the character falls
     * @param strength jump strength
     * @param costs    cost to buy
     * @param id       unique ID
     * @param name     name of character
     * @param image    file image
     */
    public Character(int gravity, int strength, int costs, int id, String name, File image) {
        this.gravity = gravity;
        this.strength = strength;
        this.costs = costs;
        this.id = id;
        this.imagePath = image.getPath();
        this.name = name;
    }

    /**
     * Creates the hitbox based on the image given by {@link #imagePath}.
     * If the creation of the hitbox from this images fails, a rectangular hitbox is created.
     */
    public void buildHitbox() {
        HitboxBuilder builder = new HitboxBuilder();
        try {
            hitbox = builder.buildHitbox(ImageIO.read(new File(imagePath)));

            Image image = new Image(new File(this.imagePath).toURI().toString(), 50, 50, true, true);
            hitbox.setStroke(new ImagePattern(image));
            hitbox.setFill(new ImagePattern(image));
        } catch (Exception e) {
            hitbox = builder.buildRectangle(250, 250);
            hitbox.setFill(Color.CRIMSON);
        }
        hitbox.setScaleX(DEFAULT_SCALE);
        hitbox.setScaleY(DEFAULT_SCALE);

    }

    /**
     * Getter for image
     *
     * @return image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Getter for gravity
     *
     * @return gravity
     */
    public int getGravity() {
        return gravity;
    }

    /**
     * Getter for jump strength
     *
     * @return jump strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Getter for costs
     *
     * @return costs
     */
    public int getCosts() {
        return costs;
    }

    /**
     * Getter for ID.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Character model "jumps up" by subtracting {@link #strength} to y-value.
     * Additionally rotate the model upwards by {@link #JUMP_ROTATION} for a maximum of {@link #MAX_JUMP_ROTATION}.
     */
    public void jump() {
        hitbox.setTranslateY(hitbox.getTranslateY() - strength);
        hitbox.setRotate(Math.max(MAX_JUMP_ROTATION, hitbox.getRotate() + JUMP_ROTATION));
    }

    /**
     * Getter for hitbox.
     *
     * @return hitbox
     */
    @Override
    public Polygon getHitbox() {
        return hitbox;
    }

    /**
     * Getter for bounds of the hitbox
     *
     * @return hitbox layout bounds
     */
    @Override
    public Bounds getBounds() {
        return hitbox.getLayoutBounds();
    }

    /**
     * Character model "falls down" by adding {@link #gravity} to y-value.
     * Additionally rotate the model downwards by {@link #FALL_ROTATION} for a maximum of {@link #MAX_FALL_ROTATION}.
     */
    @Override
    public void tick() {
        hitbox.setTranslateY(hitbox.getTranslateY() + gravity);
        hitbox.setRotate(Math.min(MAX_FALL_ROTATION, hitbox.getRotate() + FALL_ROTATION));
    }

    /**
     * Getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }
}
