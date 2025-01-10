package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import pepse.world.trees.leaf;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * the game manager class for the project, run the game
 */
public class PepseGameManager extends GameManager{
    private final int SKYLAYER = -104;
    private final int SUNLAYER = -103;
    private final int SUNHALOLAYER = -102;
    private final int TREELAYER = -101;
    private final int GROUNDLAYER = Layer.STATIC_OBJECTS;
    private final int NIGHTLAYER = Layer.FOREGROUND;
    public static final int CYCLETIME = 30;
    private final String FRUIT_TAG = "fruit";



    /**
     * init game function,
     * @param imageReader Contains a single method: readImage, which reads
     *                   an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads
     *                   a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which
     *                     returns whether
     *                      a given key is currently pressed by the user
     *                      or not. See its documentation.
     * @param windowController Contains an array of helpful, self explanatory
     *                        methods concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
        UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader,
                inputListener, windowController);
        SkyLineDayNightInit(gameObjects(),windowController);
        //terrainCall
        Terrain t = new Terrain(windowController.getWindowDimensions(),1);
        GroundInit(t,windowController);
        //addPlayer
        Vector2 spawnPlace = new Vector2(0,
                t.groundHeightAt(0) - Block.SIZE);
        GameObject player = new Avatar(spawnPlace, inputListener,imageReader);
        gameObjects().addGameObject(player);
        //add ennergyUI
        GameObject EnergyUi = energyUIInit(player);
        this.gameObjects().addGameObject(EnergyUi, Layer.UI);
        //flora
        floraInit(player,windowController,t);
    }

    /**
     * main function, run the game
     * @param args getting no args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    private void SkyLineDayNightInit(GameObjectCollection gameObjects,
        WindowController windowController) {
        //send window dimensions to the sky creating function, sky init
        GameObject sky = Sky.create(windowController.
                getWindowDimensions());
        gameObjects().addGameObject(sky, SKYLAYER);
        //nightCall
        GameObject night = Night.create(windowController.
                getWindowDimensions(),CYCLETIME);
        gameObjects().addGameObject(night, NIGHTLAYER);
        //sunCall
        GameObject sun = Sun.create(windowController.
                getWindowDimensions(),CYCLETIME);
        gameObjects().addGameObject(sun, SUNLAYER);
        //haloCall
        GameObject halo = SunHalo.create(sun);
        gameObjects().addGameObject(halo, SUNHALOLAYER);
    }

    private void GroundInit(Terrain t,
                            WindowController windowController){
        List<Block> b =  t.createInRange(0,(int)windowController.
                getWindowDimensions().x());
        for (int i = 0; i < b.size(); i++) {
            gameObjects().addGameObject(b.get(i), GROUNDLAYER);
        }
    }

    private GameObject energyUIInit(GameObject player){
        //energyText
        Supplier getEnergy = () -> ((Avatar) player).getEnergy();
        TextRenderable textEnergy = new TextRenderable("");
        textEnergy.setColor(Color.black);
        GameObject EnergyUi = new GameObject(new Vector2(0, 0),
                new Vector2(100,50),textEnergy);
        danogl.components.Component c = (float deltTime) ->
                ((TextRenderable)
                EnergyUi.renderer().getRenderable()).
                setString(getEnergy.get().toString());
        EnergyUi.addComponent(c);
        return EnergyUi;
    }

    private void floraInit(GameObject player,
                           WindowController windowController, Terrain t){
        Consumer energyzer = (Int) -> ((Avatar) player).addEnergy(10);
        Flora f = new Flora(t,energyzer);
        Renderable rendWhite = new RectangleRenderable(Color.white);
        GameObject floraManager = new GameObject(new Vector2(-100,-100),
                new Vector2(1,1),rendWhite);
        danogl.components.Component comp = (float deltTime)
                -> f.fruitChecker(deltTime);
        floraManager.addComponent(comp);
        this.gameObjects().addGameObject(floraManager);
        ArrayList<GameObject> arr = f.createInRange( 0,
                (int) windowController.getWindowDimensions().x());
        for(int i = 0; i < arr.size(); i++) {
            if(arr.get(i).getTag() != FRUIT_TAG){
                this.gameObjects().addGameObject(arr.get(i),TREELAYER);
            } else {
                this.gameObjects().addGameObject(arr.get(i), GROUNDLAYER);
            }
        }
    }


}
