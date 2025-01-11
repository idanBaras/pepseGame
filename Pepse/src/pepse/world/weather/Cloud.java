package pepse.world.weather;

import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;

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

}
