package flappyanimal.core.model;

import javafx.geometry.Bounds;
import javafx.scene.shape.Polygon;

/**
 * All objects that are on the screen and part of the game engine, must implement this interface.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public interface Entity {
    /**
     * Implementation of an action. This action gets executed every time a game time passes.
     * E.g: Move obstacles along the x-axis.
     */
    void tick();

    /**
     * Getter for hitbox of an entity
     *
     * @return hitbox as polygon
     */
    Polygon getHitbox();

    /**
     * Getter to return boundaries of an entity.
     *
     * @return boundaries
     */
    Bounds getBounds();

}
