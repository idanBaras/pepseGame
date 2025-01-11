package pepse.world.weather;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * class for rainDrop objects, spawn from cloud
 */
public class RainDrop extends GameObject {
    private final Color color = Color.BLUE;
    private final Renderable rend = new RectangleRenderable(color);
    private static final float GRAVITY = 300;
    private static final String RAIN_TAG = "rainDrop";
    private BiConsumer<GameObject, Integer> remover;
    private final int RAIN_LAYER = Layer.FOREGROUND;
    private final float TIME_TO_DIS = 1.5f;

    /**
     * builder for rainDrop
     * @param topLeftCorner pos for rainDrop
     * @param dimensions dim for rainDrop
     * @param remover to remove rainDrop
     */
    public RainDrop(Vector2 topLeftCorner, Vector2 dimensions,
                    BiConsumer<GameObject, Integer> remover) {
        super(topLeftCorner, dimensions,
                new RectangleRenderable(Color.BLUE));
        this.remover = remover;
        transform().setAccelerationY(GRAVITY);
        setTag(RAIN_TAG);
        Transition t = new Transition<>(this,
                this.renderer()::setOpaqueness,
                1f,0f,Transition.
                LINEAR_INTERPOLATOR_FLOAT,
                TIME_TO_DIS, Transition.TransitionType.TRANSITION_ONCE,
                null);
    }

    /**
     *update func used to detect Opaqueness of rainDrop
     * @param deltaTime The time elapsed, in seconds,
     * since the last frame. Can
     *be used to determine a new position/velocity by multiplying
     *this delta with the velocity/acceleration respectively
     *and adding to the position/velocity:
     *velocity += deltaTime*acceleration
     *pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(this.renderer().getOpaqueness() == 0){
            selfDis();
        }
    }

    private void selfDis(){
        remover.accept(this, RAIN_LAYER);
    }

}
