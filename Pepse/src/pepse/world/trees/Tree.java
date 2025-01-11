package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * class for creating a single tree
 */
public class Tree {
    Color logCol = new Color(100, 50, 20);
    Color leafCol = new Color(50, 200, 30);
    Renderable logRend = new RectangleRenderable(logCol);
    Renderable leafRend = new RectangleRenderable(leafCol);
    private final int MAX_SIZE = 10;
    private final int MIN_SIZE = 6;
    private final int MINUS_LEAF_PLACE = 2;
    private final int PLUS_LEAF_PLACE = 3;
    private final int MINUS_LOG_TO_LEAF_SPAWN = 5;

    /**
     * building a new tree
     * @param x place of tree
     * @return gameObjects created as the tree
     */
    public ArrayList<GameObject> createTree(float x,float heightAtX){
        ArrayList<GameObject> treeObjs = new ArrayList<GameObject>();
        Random rand = new Random();
        int treeSize = rand.nextInt(MIN_SIZE,MAX_SIZE+1);
        float fixedHeight = heightAtX-heightAtX%Block.SIZE - Block.SIZE;
        for (int i = 0; i < treeSize; i++) {
            GameObject obj = new Block(new Vector2(x,
                    fixedHeight - i*Block.SIZE), logRend);
            treeObjs.add(obj);
        }
        for (int i = treeSize-MINUS_LOG_TO_LEAF_SPAWN;
             i < treeSize; i++) {
            for (float j = x-MINUS_LEAF_PLACE*Block.SIZE;
                 j < x+PLUS_LEAF_PLACE*Block.SIZE; j+=Block.SIZE) {
                if(j != x){
                    boolean leafy = rand.nextBoolean();
                    if(leafy){
                        GameObject leaf = new leaf(new Vector2(j,
                                fixedHeight - i*Block.SIZE), leafRend);
                        treeObjs.add(leaf);
                    }
                }
            }
        }
        return treeObjs;
    }
}
