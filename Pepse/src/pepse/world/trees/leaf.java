package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Block;

import java.util.Random;
import java.util.function.Consumer;

/**
 * leaf class for the game
 */
public class leaf extends Block {
    private final int LEAF_TRANSITION_TIME = 2;
    private final float LEAF_MAX_SIZE = 35;
    private final float LEAF_MIN_SIZE = 25;
    private final String LEAF_TAG = "leaf";
    private final float RAND_MIN = 0.1f;
    private final float RAND_MAX = 0.1f;
    private final float MAX_ANGLE = 90f;


    /**
     * create leaf in current place
     * @param topLeftCorner place for leaf
     * @param renderable leaf image
     */
    public leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        this.setTag(LEAF_TAG);
        Random rand = new Random();
        ScheduledTask s= new ScheduledTask(this,
                rand.nextFloat(RAND_MIN,RAND_MAX),false,this::trans1);
        ScheduledTask s2= new ScheduledTask(this,
                rand.nextFloat(RAND_MIN,RAND_MAX),false,this::trans2);
    }

    private Transition trans1(){
        Consumer<Float> method = (Float angle) -> this.renderer().
                setRenderableAngle(angle);
        //transition
        Transition t = new Transition<>(this,method,
                0f,MAX_ANGLE,Transition.LINEAR_INTERPOLATOR_FLOAT,
                LEAF_TRANSITION_TIME, Transition.TransitionType.
                TRANSITION_BACK_AND_FORTH, null);
        return t;
    }

    private Transition trans2(){
        Consumer<Float> method2 = (Float width) -> this.
                setDimensions(new Vector2(width,width));
        //transition
        Transition t2 = new Transition<>(this,method2,
                LEAF_MIN_SIZE,LEAF_MAX_SIZE,Transition.LINEAR_INTERPOLATOR_FLOAT,
                LEAF_TRANSITION_TIME, Transition.TransitionType.
                TRANSITION_BACK_AND_FORTH, null);
        return t2;
    }

}
