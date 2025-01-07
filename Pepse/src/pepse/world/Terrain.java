package pepse.world;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * class for making all the ground block to create terrain
 */
public class Terrain {
    private float groundHeightAtX0;

    /**
     * builder for terrain
     * @param windowDimensions window dimensions of the game
     * @param seed random seed for world creation
     */
    public Terrain(Vector2 windowDimensions, int seed){
        groundHeightAtX0 =(2/3)*windowDimensions.y();
    }

    /**
     * return the ground height at given x value
     * @param x the x value in question
     * @return the height of the ground at said x value
     */
    public float groundHeightAt(float x)
    {
        //temporary implemention
        return groundHeightAtX0;
    }



}
