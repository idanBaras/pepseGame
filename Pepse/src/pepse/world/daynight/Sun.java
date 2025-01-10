package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

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
        final int SUN_DIM = 100;
        final int TWO = 2;
        final int SUN_POS_MODIFIER = 200;
        final float MAX_ANGLE = 360f;
        final String SUN_TAG = "Sun";
        Renderable r = new OvalRenderable(Color.YELLOW);
        Vector2 dims = new Vector2(SUN_DIM,SUN_DIM);
        Vector2 startPos = new Vector2(windowDimensions.x()/TWO,
                windowDimensions.y()/TWO-SUN_POS_MODIFIER);
        Vector2 cycleCenter = new Vector2(windowDimensions.x()/TWO,
                windowDimensions.y()/TWO+SUN_POS_MODIFIER);
        GameObject sun = new GameObject(startPos,dims,r);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        Consumer<Float> method = (Float angle) -> sun.setCenter
                (startPos.subtract(cycleCenter)
                        .rotated(angle)
                        .add(cycleCenter));
        //transition
        Transition t = new Transition<>(sun,method,
        MAX_ANGLE,0f,Transition.LINEAR_INTERPOLATOR_FLOAT,
                TWO* PepseGameManager.CYCLE_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
        return sun;
    }

}
