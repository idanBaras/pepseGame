package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 *class represent night time implemention
 */
public class Night {
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final Float DAY_OPACITY = 0f;
    /**
     * create new night object
     * @param windowDimensions window dims to create night obj
     * @param cycleLength length of nightDayCycle
     * @return tyhe night gameObject
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        Vector2 startPos = new Vector2(0,0);
        Vector2 dims = windowDimensions;
        Renderable black = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(startPos,dims,black);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("night");
        Transition t = new Transition<>(night,night.renderer()::setOpaqueness,
                DAY_OPACITY,MIDNIGHT_OPACITY,Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        return night;
    }





}
