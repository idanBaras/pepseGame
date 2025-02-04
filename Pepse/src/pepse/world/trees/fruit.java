package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * fruit class object
 */
public class fruit extends GameObject {
    private final String PLAYER_TAG = "player";
    private final String EATEN_TAG = "eaten";
    private final String FRUIT_TAG = "fruit";
    private Consumer<Integer> energyzer;
    /**
     * builder for fruit object
     * @param topLeftCorner pos of fruit
     * @param dimensions dim for fruit
     * @param renderable rend for fruit
     */
    public fruit(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable, Consumer<Integer> energyzer) {
        super(topLeftCorner, dimensions, renderable);
        setTag(FRUIT_TAG);
        this.energyzer = energyzer;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag() == PLAYER_TAG){
            setTag(EATEN_TAG);
            energyzer.accept(0);
        }
    }
}
