package flappyanimal.core.model;

import flappyanimal.core.HitboxBuilder;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Represents a obstacle on the board. It can have different properties such as image, height and starting position.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class Obstacle extends Rectangle implements Entity {
    /**
     * Maximum height to cover
     */
    public static final double MAX_SCALE = 2.6d;
    /**
     * Default height for an obstacle
     */
    private static final double DEFAULT_HEIGHT = 180;
    /**
     * Default width for an obstacle
     */
    private static final double DEFAULT_WIDTH = 160;

    /**
     * 0.172d and 0.2d seems like the magic-numbers for scaling the polygon with the image
     */
    private static final double DEFAULT_SCALE_X = 0.2d;
    private static final double DEFAULT_SCALE_Y = 0.172d;
    /**
     * Amount to move after each tick
     */
    private static final int MOVEMENT = -3;
    /**
     * Hitbox of the obstacle's image
     */
    private Polygon hitbox;

    /**
     * Factory method to create a new obstacle based on input parameters.
     *
     * @param imagePath   image for the obstacle
     * @param x           absolute x-position on the board
     * @param y           absolute y-position on the board
     * @param flip        true, then flip whole obstacle 180 degree (upside-down)
     * @param heightScale random modifier to create variety in height
     * @return obstacle with given properties
     */
    public static Obstacle create(String imagePath, double x, double y, boolean flip, double heightScale) {
        return new Obstacle(imagePath, x, y, flip, heightScale);
    }

    /**
     * Creates a new obstacle with a hitbox outline non transparent pixels.
     * Hitbox gets created with {@link HitboxBuilder#buildHitbox(BufferedImage)}.
     *
     * @param imagePath   image for the obstacle
     * @param x           absolute x-position on the board
     * @param y           absolute y-position on the board
     * @param flip        true, then flip whole obstacle 180 degree (upside-down)
     * @param heightScale random modifier to create variety in height
     */
    private Obstacle(String imagePath, double x, double y, boolean flip, double heightScale) {
        setAppearance(imagePath, heightScale);

        this.setX(x);
        this.setViewOrder(1);

        buildHitbox(imagePath, heightScale);

        setOrientation(y, flip);
    }

    /**
     * Sets y position and rotates everything.
     *
     * @param y    y position of the obstacle
     * @param flip if true, rotates everything by 180 degree.
     */
    private void setOrientation(double y, boolean flip) {
        if (flip) {
            this.setY(y);
            this.setRotate(180);
            hitbox.setTranslateY(-hitbox.getBoundsInParent().getMinY());
        } else {
            this.setTranslateY(y - this.getHeight());
            this.setRotate(0);
            hitbox.setTranslateY(y - hitbox.getBoundsInParent().getMaxY());
        }
    }

    /**
     * Loads the image in the requested height. If loading the image fails, a gray box is used as replacement.
     * Additionally sets the width and height from the image or in the error case from the default values.
     *
     * @param imagePath   path to image
     * @param heightScale random modifier to create variety in height
     */
    private void setAppearance(String imagePath, double heightScale) {
        try {
            Image image = new Image(imagePath, DEFAULT_WIDTH, DEFAULT_HEIGHT * heightScale, false, false);
            this.setFill(new ImagePattern(image));
            this.setHeight(image.getHeight());
            this.setWidth(image.getWidth());
        } catch (Exception ignored) {
            this.setWidth(DEFAULT_WIDTH);
            this.setHeight(DEFAULT_HEIGHT * heightScale);
            this.setFill(Color.GRAY);
        }
    }

    /**
     * Creates the hitbox based on the image given by imagePath.
     * If the creation of the hitbox from this images fails, a rectangular hitbox is created.
     */
    private void buildHitbox(String imagePath, double heightScale) {
        HitboxBuilder builder = new HitboxBuilder();
        try {
            hitbox = builder.buildHitbox(ImageIO.read(this.getClass().getResource(imagePath)));

            //on the x-axis the rectangle and polygon have a weird offset.
            //to get both of them algin, subtract the with of the rectangle.
            //to get them algin almost perfect, subtract the width by an additional 9.
            hitbox.translateXProperty().bind(this.xProperty().subtract(this.getWidth() * 2).subtract(9));
            hitbox.rotateProperty().bind(this.rotateProperty());

            hitbox.setScaleY(DEFAULT_SCALE_Y * heightScale);
            hitbox.setScaleX(DEFAULT_SCALE_X);

        } catch (Exception e) { //fallback hitbox
            hitbox = builder.buildRectangle(this.getWidth(), this.getHeight());
            hitbox.translateXProperty().bind(this.xProperty());
            hitbox.rotateProperty().bind(this.rotateProperty());
        }
    }

    /**
     * Getter for polygon hitbox
     *
     * @return hitbox
     */
    @Override
    public Polygon getHitbox() {
        return hitbox;
    }

    /**
     * Move rectangle {@link #MOVEMENT} on x-axis
     */
    @Override
    public void tick() {
        this.setX(this.getX() + MOVEMENT);
    }

    /**
     * Getter for rectangle boundaries
     *
     * @return boundaries
     */
    @Override
    public Bounds getBounds() {
        return super.getLayoutBounds();
    }
}
