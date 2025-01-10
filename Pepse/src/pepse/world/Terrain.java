package pepse.world;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 * class for making all the ground block to create terrain
 */
public class Terrain {
    private float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR =
            new Color(212, 123, 74);
    private int groundLevel;
    private int seed;
    private static final int TERRAIN_DEPTH = 20;
    private static final int NOISE_MODIFIER = 10;
    private static final float MODIFIER = 0.67f;
    private static final String GROUND_TAG = "ground";

    /**
     * builder for terrain
     * @param windowDimensions window dimensions of the game
     * @param seed random seed for world creation
     */
    public Terrain(Vector2 windowDimensions, int seed){
        groundHeightAtX0 =windowDimensions.y()*MODIFIER;
        groundLevel = (int)windowDimensions.y();
        this.seed = seed;
    }

    /**
     * return the ground height at given x value
     * @param x the x value in question
     * @return the height of the ground at said x value
     */
    public float groundHeightAt(float x)
    {
        NoiseGenerator n = new NoiseGenerator(seed,groundLevel);
        float noise = (float) n.noise(x, Block.SIZE*NOISE_MODIFIER);
        return groundHeightAtX0 + noise;
    }

    /**
     * create blocks in range
     * @param minX min range
     * @param maxX max range
     * @return list of created blocks
     */
    public List<Block> createInRange(int minX, int maxX) {
        int fixedMinX = minX - minX%Block.SIZE;
        int fixedMaxX = maxX - maxX%Block.SIZE;
        List<Block> blocks = new ArrayList<>();
        RectangleRenderable r = new RectangleRenderable(ColorSupplier.
                approximateColor(BASE_GROUND_COLOR));
        for (int i = fixedMinX; i < fixedMaxX; i+=Block.SIZE) {
            int startJ = (((int)groundHeightAt(i))/Block.SIZE)*Block.SIZE;
            for (int j = startJ; j < startJ + TERRAIN_DEPTH*Block.SIZE;
                 j+=Block.SIZE) {
                Vector2 v2 = new Vector2(i, j);
                Block block = new Block(v2,r);
                block.setTag(GROUND_TAG);
                blocks.add(block);
            }
        }
        return blocks;
    }


}
