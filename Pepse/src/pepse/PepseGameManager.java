package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.*;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.weather.Cloud;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**
 * the game manager class for the project, run the game
 */
public class PepseGameManager extends GameManager{
    private static final int SKYLAYER = -104;
    private static final int SUN_LAYER = -103;
    private static final int SUN_HALO_LAYER = -102;
    private static final int TREE_LAYER = -101;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    /**
     * cycle time of the world
     */
    public static final int CYCLE_TIME = 30;
    private static final int CLOUD_OBJ_CORD = -100;
    private static final int ENERGY_UI_X = 100;
    private static final int ENERGY_UI_Y = 50;
    private static final int CLOUD_Y = 60;
    private static final int EXTRA_FRUIT_ENERGY = 10;
    private static final float TERRAIN_SPAWN_MOD = 0.75f;
    /**
     * seed for the world
     */
    public static final int SEED = 2;


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
     * @param windowController Contains an array of helpful, self-explanatory
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
        Terrain t = new Terrain(windowController.getWindowDimensions(),SEED);
        GroundInit(t,windowController);
        //addPlayer
        float midScreen = windowController.getWindowDimensions().x()/2;
        Vector2 spawnPlace = new Vector2(0,
                t.groundHeightAt(0) - Block.SIZE);
        Avatar player = new Avatar(spawnPlace, inputListener,imageReader);
        gameObjects().addGameObject(player);
        //add energyUI
        GameObject EnergyUi = energyUIInit(player);
        EnergyUi.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(EnergyUi, Layer.UI);
        //flora
        floraInit(player,windowController,t);
        //cameraInit
        setCamera(new Camera(player, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        //cloud init
        BiConsumer<GameObject, Integer> remover = (obj,layer) ->
                gameObjects().removeGameObject(obj,layer);
        BiConsumer<GameObject, Integer> adder = (obj,layer) ->
                gameObjects().addGameObject(obj,layer);
        cloudInit(windowController, player,remover,adder);
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
                getWindowDimensions(),CYCLE_TIME);
        gameObjects().addGameObject(night, NIGHT_LAYER);
        //sunCall
        GameObject sun = Sun.create(windowController.
                getWindowDimensions(),CYCLE_TIME);
        gameObjects().addGameObject(sun, SUN_LAYER);
        //haloCall
        GameObject halo = SunHalo.create(sun);
        gameObjects().addGameObject(halo, SUN_HALO_LAYER);
    }

    private void GroundInit(Terrain t,
                            WindowController windowController){
        List<Block> b =  t.createInRange((int)-windowController.
                getWindowDimensions().x(),(int)windowController.
                getWindowDimensions().x());
        for (int i = 0; i < b.size(); i++) {
            gameObjects().addGameObject(b.get(i), GROUND_LAYER);
        }
    }

    private GameObject energyUIInit(GameObject player){
        //energyText
        Supplier getEnergy = () -> ((Avatar) player).getEnergy();
        TextRenderable textEnergy = new TextRenderable("");
        textEnergy.setColor(Color.black);
        GameObject EnergyUi = new GameObject(new Vector2(0, 0),
                new Vector2(ENERGY_UI_X,ENERGY_UI_Y),textEnergy);
        danogl.components.Component c = (float deltTime) ->
                ((TextRenderable)
                EnergyUi.renderer().getRenderable()).
                setString(getEnergy.get().toString());
        EnergyUi.addComponent(c);
        return EnergyUi;
    }

    private void floraInit(GameObject player,
                           WindowController windowController, Terrain t){
        Consumer<Integer> energyzer = (Int) -> ((Avatar) player).
                addEnergy(EXTRA_FRUIT_ENERGY);
        Flora f = new Flora(t,energyzer);
        Renderable rendWhite = new RectangleRenderable(Color.white);
        GameObject floraManager = new GameObject(new Vector2(0,0),
                new Vector2(1,1),rendWhite);
        //fruit comp
        danogl.components.Component comp = (float deltTime)
                -> f.fruitChecker(deltTime);
        floraManager.addComponent(comp);
        //extra terrain flora comp
        int minX = (int)-windowController.getWindowDimensions().x();
        int maxX = (int)windowController.getWindowDimensions().x();
        int[] terrainBound = new int[2];
        terrainBound[0] = minX;
        terrainBound[1] = maxX;
        danogl.components.Component extraComp = (float deltTime)
                -> extraTerrainFlora(t,f,player,windowController,terrainBound);
        floraManager.addComponent(extraComp);
        this.gameObjects().addGameObject(floraManager);
        //flora created
        ArrayList<GameObject> arr = f.createInRange( (int) -windowController
                        .getWindowDimensions().x(),
                (int) windowController.getWindowDimensions().x());
        for(int i = 0; i < arr.size(); i++) {
            final String FRUIT_TAG = "fruit";
            if(arr.get(i).getTag() != FRUIT_TAG){
                this.gameObjects().addGameObject(arr.get(i),TREE_LAYER);
            } else {
                this.gameObjects().addGameObject(arr.get(i), GROUND_LAYER);
            }
        }
    }

    private void cloudInit(WindowController windowController,
        Avatar player, BiConsumer<GameObject,Integer> remover,
                           BiConsumer<GameObject,Integer> adder){
        Cloud c = new Cloud(remover,adder);
        Vector2 startPos = new Vector2(0,CLOUD_Y);
        GameObject[][] arr= c.createCloud(startPos);
        for(int i=0;i<arr.length;i++){
            for (int j=0;j<arr[i].length;j++){
                if(arr[i][j] != null){
            arr[i][j].setCoordinateSpace(CoordinateSpace.WORLD_COORDINATES);
                    gameObjects().addGameObject(arr[i][j], SUN_LAYER);
                }
            }
        }

        BiFunction<WindowController, GameObject, Integer> cloudCheck =
                (w, p) -> c.cloudInFrame(p,w);
        Renderable r = new OvalRenderable(Color.white);
        GameObject cloudCheckObj = new GameObject(new Vector2(CLOUD_OBJ_CORD,
                CLOUD_OBJ_CORD), new Vector2(1,1),r);
        danogl.components.Component comp = (float deltTime) ->
                cloudCheck.apply(windowController,player);
        cloudCheckObj.addComponent(comp);

        Function<Avatar, Integer> rainFall = (p) -> c.rain(p.isJumping());
        danogl.components.Component comp2 = (float deltTime) ->
                rainFall.apply(player);
        cloudCheckObj.addComponent(comp2);
        gameObjects().addGameObject(cloudCheckObj, Layer.UI);
    }

    private void extraTerrainFlora(Terrain t, Flora f,
          GameObject player,WindowController windowController,
          int[] arr){

        int windowX = (int)windowController.getWindowDimensions().x();
        if(player.getCenter().x()-arr[0]<windowX*(TERRAIN_SPAWN_MOD)){
            List<Block> terArr = t.createInRange(arr[0]-windowX,arr[0]);
            ArrayList<GameObject> floArr = f.createInRange
                    (arr[0]-windowX,arr[0]);
            addObj(terArr);
            addObj(floArr);
            arr[0] = arr[0]-windowX;
        }
        if(arr[1]-player.getCenter().x()<windowX*(TERRAIN_SPAWN_MOD)){
            List<Block> terArr = t.createInRange(arr[1],arr[1] + windowX);
            ArrayList<GameObject> floArr = f.createInRange
                    (arr[1],arr[1] + windowX);
            addObj(terArr);
            addObj(floArr);
            arr[1] = arr[1] + windowX;
        }
    }

    private void addObj(ArrayList<GameObject> arr){
        for(int i = 0; i < arr.size(); i++) {
            final String FRUIT_TAG = "fruit";
            if(arr.get(i).getTag() != FRUIT_TAG){
                this.gameObjects().addGameObject(arr.get(i),TREE_LAYER);
            } else {
                this.gameObjects().addGameObject(arr.get(i), GROUND_LAYER);
            }
        }
    }
    private void addObj(List<Block> arr){
        for (int i = 0; i < arr.size(); i++) {
            this.gameObjects().addGameObject(arr.get(i), GROUND_LAYER);
        }
    }

}
