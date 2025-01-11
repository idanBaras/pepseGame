package pepse.world.weather;


import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

/**
 * cloud block building for cloud object
 */
public class CloudUnit extends Block {
    private static final float VELOCITY_X = 300;

    /**
     * builder for cloud unit
     * @param topLeftCorner pos for the unit
     * @param renderable rend for unit
     */
    public CloudUnit(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        setTag("cloud");
        transform().setVelocityX(VELOCITY_X);
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

}
