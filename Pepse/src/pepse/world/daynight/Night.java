package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 *class represent nighttime implementation
 */
public class Night {
    /**
     * create new night object
     * @param windowDimensions window dims to create night obj
     * @param cycleLength length of nightDayCycle
     * @return tyhe night gameObject
     */
    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength){
        final Float MIDNIGHT_OPACITY = 0.5f;
        final Float DAY_OPACITY = 0f;
        final String NIGHT_TAG = "Night";
        Vector2 startPos = new Vector2(0,0);
        Vector2 dims = windowDimensions;
        Renderable black = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(startPos,dims,black);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        Transition t = new Transition<>(night,night.renderer()::setOpaqueness,
                DAY_OPACITY,MIDNIGHT_OPACITY,Transition.
                CUBIC_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.
                TRANSITION_BACK_AND_FORTH,
                null);
        return night;
    }





}
