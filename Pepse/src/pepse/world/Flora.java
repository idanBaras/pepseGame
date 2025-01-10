package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.trees.Tree;
import pepse.world.trees.fruit;
import pepse.world.trees.leaf;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * will create trees in places in the world
 */
public class Flora {
    private ArrayList<fruit> fruits;
    private ArrayList<Float> fruitsTimer;
    private final String LEAF_TAG = "leaf";
    private final String FRUIT_TAG = "fruit";
    private Color fruitColor = Color.RED;
    private Terrain terrain;
    private Consumer energyzer;

    /**
     * builder for flora class
     * @param t terrain ref
     * @param energyzer energyFunc
     */
    public Flora(Terrain t, Consumer energyzer) {
        this.terrain = t;
        this.fruits = new ArrayList<>();
        this.fruitsTimer = new ArrayList<>();
        this.energyzer = energyzer;
    }

    /**
     * create flora in given values
     * @param minX min value for flora
     * @param maxX max value for flora
     * @return list of objects to add
     */
    public ArrayList<GameObject> createInRange(int minX, int maxX) {
        Random rand = new Random();
        Tree tree = new Tree();
        ArrayList<GameObject> treeObjs = new ArrayList<>();
        for (int i = minX; i < maxX; i+=5*Block.SIZE) {
            boolean treeBool = rand.nextBoolean();
            if (treeBool){
                ArrayList<GameObject> arr = tree.createTree(i,
                        terrain.groundHeightAt(i));
                for (GameObject obj : arr) {
                    treeObjs.add(obj);
                }
            }
        }
        for (int i=0; i<treeObjs.size(); i++) {
            if(treeObjs.get(i).getTag() == LEAF_TAG){
                boolean fruity = rand.nextBoolean();
                if(fruity){
                    Renderable r = new OvalRenderable(fruitColor);
                    fruit fr = new fruit(treeObjs.get(i).getTopLeftCorner(),
                            treeObjs.get(i).getDimensions(),r,energyzer);
                    treeObjs.add(fr);
                    fruits.add(fr);
                    fruitsTimer.add(0f);
                }
            }
        }
        return treeObjs;
    }

    /**
     * check fruit state
     * @param deltaTime time passed
     */
    public void fruitChecker(float deltaTime){
        for (int i = 0; i < fruits.size(); i++) {
            if(fruits.get(i).getTag() != FRUIT_TAG){
                if(fruitsTimer.get(i) == 0f){
                    fruits.get(i).setCenter(new Vector2(fruits.get(i).
                            getCenter().x(),
                            fruits.get(i).getCenter().y()-2000f));
                    fruitsTimer.set(i,deltaTime);
                } else {
                    if(fruitsTimer.get(i) >= PepseGameManager.CYCLE_TIME){
                        fruits.get(i).setTag(FRUIT_TAG);
                        fruits.get(i).setCenter(new Vector2(fruits.get(i).
                                getCenter().x(), fruits.get(i).
                                getCenter().y()+2000f));
                        fruitsTimer.set(i,0f);
                    } else {
                        fruitsTimer.set(i,deltaTime + fruitsTimer.get(i));
                    }
                }
            }
        }
    }

}
