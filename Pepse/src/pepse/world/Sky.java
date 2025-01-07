package pepse.world;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * the class that makes the sky
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");



    /**
     * create the sky object
     * @param windowDimensions window dims for the sky object
     * @return the sky game object
     */
    public static GameObject create(Vector2 windowDimensions){
        //create sky game object
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        //move the object with camera
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        //setting tag for debug purposes only
        sky.setTag("sky");
        return sky;
    }


}
