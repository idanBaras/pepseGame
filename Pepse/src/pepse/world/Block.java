package pepse.world;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.*;

/***
 * the Block class for blocks in game
 */
public class Block extends GameObject {
    /**
     * the size of block in game
     */
    public static final int SIZE = 30;

    /**
     * block create func
     * @param topLeftCorner top left corner pos for block
     * @param renderable image for block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }


}
