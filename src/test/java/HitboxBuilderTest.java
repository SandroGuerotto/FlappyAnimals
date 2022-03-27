import flappyanimal.core.HitboxBuilder;
import javafx.scene.shape.Polygon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class HitboxBuilderTest {

    @Mock
    private BufferedImage buffImgMock;
    @Mock
    private WritableRaster rasterMock;
    @Mock
    private DataBufferByte buffMock;

    private HitboxBuilder cut;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cut = new HitboxBuilder();
    }

    /**
     * Tests if a rectangular hitbox gets created.
     */
    @Test
    void testBuildHitboxSimple() {
        //only alpha channel is relevant: 2 rest is 1
        byte[] testPixels = new byte[]{
                2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1,
                2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1,
                2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1
        };

        List<Double> exp = Arrays.asList(0d, 0d, 0d, 1d, 0d, 2d, 3d, 2d, 3d, 1d, 3d, 0d);

        when(buffImgMock.getRaster()).thenReturn(rasterMock);
        when(rasterMock.getDataBuffer()).thenReturn(buffMock);
        when(buffMock.getData()).thenReturn(testPixels);
        when(buffImgMock.getWidth()).thenReturn(4);
        Polygon act = cut.buildHitbox(buffImgMock);

        //6x and 6y points
        assertEquals(12, act.getPoints().size());
        assertEquals(exp, act.getPoints());
    }

    /**
     * Tests if a complex hitbox gets created. Each corner pixel is transparent.
     * Hitbox will look like this:
     * 0110
     * 1111
     * 0110
     */
    @Test
    void testBuildHitboxComplex() {
        //only alpha channel is relevant => 2. rest is 1
        // argb = 2111
        byte[] testPixels = new byte[]{
                0, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 0, 1, 1, 1,
                2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1,
                0, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 0, 1, 1, 1,
        };

        List<Double> exp = Arrays.asList(1d, 0d, 0d, 1d, 1d, 2d, 4d, 2d, 5d, 1d, 4d, 0d);

        when(buffImgMock.getRaster()).thenReturn(rasterMock);
        when(rasterMock.getDataBuffer()).thenReturn(buffMock);
        when(buffMock.getData()).thenReturn(testPixels);
        when(buffImgMock.getWidth()).thenReturn(6);

        Polygon act = cut.buildHitbox(buffImgMock);

        //6x and 6y points
        assertEquals(12, act.getPoints().size());
        assertEquals(exp, act.getPoints());
    }

    @Test
    void testBuildRectangle() {
        List<Double> exp = Arrays.asList(0d, 0d, 200d, 0d, 200d, 150d, 0d, 150d);

        Polygon act = cut.buildRectangle(200, 150);
        assertEquals(8, act.getPoints().size());
        assertEquals(exp,act.getPoints());
    }

}
