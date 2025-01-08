package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * the sun object class
 */
public class Sun {

    /**
     * create sun object
     * @param windowDimensions window dimensions of screen
     * @param cycleLength cycleTime dayNight
     * @return the sun GameObject
     */
    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength){
        Renderable r = new OvalRenderable(Color.YELLOW);
        Vector2 dims = new Vector2(100,100);
        Vector2 startPos = new Vector2(windowDimensions.x()/2,
                windowDimensions.y()/2-200);
        Vector2 cycleCenter = new Vector2(windowDimensions.x()/2,
                windowDimensions.y()/2+200);
        GameObject sun = new GameObject(startPos,dims,r);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        Consumer<Float> method = (Float angle) -> sun.setCenter
                (startPos.subtract(cycleCenter)
                        .rotated(angle)
                        .add(cycleCenter));
        //transition
        Transition t = new Transition<>(sun,method,
                360f,0f,Transition.LINEAR_INTERPOLATOR_FLOAT,
                2*cycleLength, Transition.TransitionType.TRANSITION_LOOP,
                null);
        return sun;
    }

}
