package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.Component;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

/**
 * SunHalo class for the suns halo object
 */
public class SunHalo {

    /**
     * create the sunHalo object
     * @param sun the sun object of the game
     * @return the sunHalo object
     */
    public static GameObject create(GameObject sun){
        final String HALO_TAG = "Halo";
        Color haloColor = new Color(255, 255, 0, 20);
        Renderable r = new OvalRenderable(haloColor);
        Vector2 dims = new Vector2(150,150);
        Vector2 startPos = new Vector2(sun.getCenter().x(),
                sun.getCenter().y());
        GameObject halo = new GameObject(startPos,dims,r);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag(HALO_TAG);
        halo.setCenter(sun.getCenter());
        danogl.components.Component c = (float deltTime)
                -> halo.setCenter(sun.getCenter());
        sun.addComponent(c);
        return halo;
    }

}
