package flappyanimal.core;

import javafx.scene.shape.Polygon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

/**
 * HitboxBuilder can create a polygon based on an images non transparent pixels
 * or by giving a width and height for a rectangular polygon.
 *
 * @author Landrit Ahmeti, Sandro Guerotto, Safiyya Waldburger, David Gerber
 * @version 1.0
 */
public class HitboxBuilder {
    /**
     * Takes an image and create a polygon in shape of the outline of the image.
     *
     * @param image Image for the hitbox
     * @return created polygon
     */
    public Polygon buildHitbox(BufferedImage image) {
        Polygon hitbox = new Polygon();
        List<Point> points = convertToArrayLocation(image);

        // Insert all points in the correct order
        // Polygon takes its points from top to bottom left to bottom to top right.
        // In that case, we insert all points from the left side (top-bottom)
        for (int i = 0; i < points.size(); i += 2) {
            hitbox.getPoints().add(points.get(i).getX());
            hitbox.getPoints().add(points.get(i).getY());
        }
        // And then insert from bottom right to top right
        for (int i = points.size() - 1; i > 0; i -= 2) {
            hitbox.getPoints().add(points.get(i).getX());
            hitbox.getPoints().add(points.get(i).getY());
        }
        return hitbox;
    }

    /**
     * Creates a list of points outlining the given image.
     * Each row of pixels gets processed as followed:
     * If the pixel is transparent (value = 0) then skip it and proceed to the next.
     * Save first non-transparent pixel as x,y as for start point.
     * Add end point with same y but incremented x.
     * If within a row a pixel is transparent, the end point is saved and the whole process repeats.
     * At the end of each row y increments and next row gets processed.
     *
     * @param inputImage input image to create the outline
     * @return list of points outlining the image
     */
    private List<Point> convertToArrayLocation(BufferedImage inputImage) {

        final byte[] bytes = ((DataBufferByte) inputImage.getRaster().getDataBuffer()).getData(); // get pixel value as single array from buffered Image
        final int width = inputImage.getWidth(); //get image width value

        List<Point> points = new ArrayList<>();
        Point opposite = null;
        //this loop allocates pixels value to x,y Points. +4 for 4 channels: argb
        for (int pixel = 0, row = 0, col = 0; pixel < bytes.length; pixel += 4) {
            if (bytes[pixel] != 0) {
                // only first and last colored pixels are relevant
                if (opposite == null) {
                    points.add(new Point(col, row));
                    opposite = new Point(col, row);
                    points.add(opposite);
                }
                opposite.x = col;
            } else {
                if (opposite != null) {
                    opposite = null;
                }

            }

            col++;
            if (col == width) {
                col = 0;
                row++;
                opposite = null;
            }
        }
        return points;
    }

    /**
     * Creates a Polygon as a rectangle with given width and height
     *
     * @param width  rectangle width
     * @param height rectangle height
     * @return polygon in shape of a rectangle
     */
    public Polygon buildRectangle(double width, double height) {
        return new Polygon(0, 0, width, 0, width, height, 0, height);
    }
}
