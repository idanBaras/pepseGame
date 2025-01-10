package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.sql.Time;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * the game manager class for the project, run the game
 */
public class PepseGameManager extends GameManager{
    private final int SKYLAYER = -103;
    private final int SUNLAYER = -102;
    private final int SUNHALOLAYER = -101;
    private final int GROUNDLAYER = Layer.STATIC_OBJECTS;
    private final int NIGHTLAYER = Layer.FOREGROUND;
    private final int CYCLETIME = 30;



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
        //send window dimensions to the sky creating function, sky init
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, SKYLAYER);
        //terrainCall
        Terrain t = new Terrain(windowController.getWindowDimensions(),1);
        List<Block> b =  t.createInRange(0,(int)windowController.
                getWindowDimensions().x());
        for (int i = 0; i < b.size(); i++) {
            gameObjects().addGameObject(b.get(i), GROUNDLAYER);
        }
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
        //addPlayer
        Vector2 spawnPlace = new Vector2(0, t.groundHeightAt(0) - Block.SIZE);
        GameObject player = new Avatar(spawnPlace,inputListener,imageReader);
        gameObjects().addGameObject(player);
        //energyText
        Supplier getEnergy = () -> ((Avatar) player).getEnergy();
        TextRenderable textEnergy = new TextRenderable("");
        textEnergy.setColor(Color.black);
        GameObject EnergyUi = new GameObject(new Vector2(0, 0),
                new Vector2(100,50),textEnergy);
        danogl.components.Component c = (float deltTime) -> ((TextRenderable)
EnergyUi.renderer().getRenderable()).setString(getEnergy.get().toString());
        EnergyUi.addComponent(c);
        this.gameObjects().addGameObject(EnergyUi, Layer.UI);
    }

    /**
     * main function, run the game
     * @param args getting no args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }


}
