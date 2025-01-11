package pepse.world.weather;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * class for in game cloud object
 */
public class Cloud {
    private static final Color BASE_CLOUD_COLOR = new Color(255,
            255, 255);
    private final Renderable cloudRend = new RectangleRenderable(ColorSupplier.
            approximateMonoColor(BASE_CLOUD_COLOR));

    private final int[][] cloudMatrix = {{0, 1, 1, 1, 1, 0},
                                         {1, 1, 1, 1, 1, 1},
                                         {1, 1, 1, 1, 1, 1},
                                         {1, 1, 1, 1, 1, 1},
                                         {0, 1, 1, 1, 1, 0},
                                         {0, 0, 1, 0, 0, 0}};
    private final int MIN_X_MATRIX = 1;
    private GameObject[][] cloudBlocks;
    private BiConsumer<GameObject, Integer> remover;
    private BiConsumer<GameObject, Integer> adder;
    private final float DROP_DIM = 15f;
    private final int RAIN_LAYER = Layer.FOREGROUND;
    private final int RAIN_SPAWN_MODIFIER = 3;

    public Cloud(BiConsumer<GameObject, Integer> remover,
                 BiConsumer<GameObject, Integer> adder) {
        this.remover = remover;
        this.adder = adder;
    }

    /**
     * create a cloud
     * @param pos pos for cloud
     * @return the objects created
     */
    public GameObject[][] createCloud(Vector2 pos){
        cloudBlocks = new GameObject[cloudMatrix.length]
                [cloudMatrix[0].length];
        for (int i = 0; i < cloudMatrix.length; i++) {
            for (int j = 0; j < cloudMatrix[0].length; j++) {
                if (cloudMatrix[i][j] == 1) {
                    Vector2 position = new Vector2(pos.x() + i* Block.SIZE,
                            pos.y() + j* Block.SIZE);
                    GameObject cloudU = new CloudUnit(position,cloudRend);
                    cloudBlocks[i][j] = cloudU;
                } else {
                    cloudBlocks[i][j] = null;
                }
            }
        }
        return cloudBlocks;
    }

    /**
     * check if cloud need to reset if so return 1 else return 0
     * @param windowController for window dims
     * @param playerPos to get camera pos
     * @return if the cloud moved
     */
    public int cloudInFrame(WindowController windowController,
                             Vector2 playerPos){
        Vector2 minCloudPos = cloudBlocks[MIN_X_MATRIX][0].getCenter();
        Vector2 cameraPos = new Vector2(playerPos.x() + windowController.
                getWindowDimensions().x()/2, playerPos.y());
        if(cameraPos.x() + Block.SIZE <minCloudPos.x()){
            for(int i=0;i<cloudBlocks.length;i++){
                for (int j=0;j<cloudBlocks[i].length;j++){
                    if(cloudBlocks[i][j] != null){
                        Vector2 newPos = new Vector2(cloudBlocks[i][j].
getCenter().x() - windowController.getWindowDimensions().x(),
                                cloudBlocks[i][j].getCenter().y());
                        cloudBlocks[i][j].setCenter(newPos);
                    }
                }
            }
            return 1;
        }
        return 0;
    }

    /**
     *function create rain
     * @param jumped if player jumped
     * @return return 1 for rain, 0 otherwise
     */
    public int rain(boolean jumped) {
        if (jumped) {
            Random rand = new Random();
            int drops = rand.nextInt(1,3);
            if(drops == 1){
                Vector2 center = new Vector2(cloudBlocks[MIN_X_MATRIX][0].
                getCenter().x() + RAIN_SPAWN_MODIFIER*Block.SIZE,
                cloudBlocks[MIN_X_MATRIX][0].getCenter().y());
                Vector2 dims = new Vector2(DROP_DIM, DROP_DIM);
                GameObject drop = new RainDrop(center, dims, remover);
                adder.accept(drop, RAIN_LAYER);
            } else {
                Vector2 center = new Vector2(cloudBlocks[MIN_X_MATRIX][0].
                        getCenter().x() + RAIN_SPAWN_MODIFIER*Block.SIZE,
                        cloudBlocks[MIN_X_MATRIX][0].getCenter().y());
                Vector2 center2 = new Vector2(cloudBlocks[MIN_X_MATRIX][0].
                        getCenter().x(),
                        cloudBlocks[MIN_X_MATRIX][0].getCenter().y());
                Vector2 dims = new Vector2(DROP_DIM, DROP_DIM);
                GameObject drop = new RainDrop(center, dims, remover);
                GameObject drop2 = new RainDrop(center2, dims, remover);
                adder.accept(drop, RAIN_LAYER);
                adder.accept(drop2, RAIN_LAYER);
            }
            return 1;
        }
        return 0;
    }

}
