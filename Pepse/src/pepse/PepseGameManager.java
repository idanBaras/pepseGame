package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Sky;

/**
 * the game manager class for the project, run the game
 */
public class PepseGameManager extends GameManager{
    private final int skyLayer = 1;




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
        //adding sky object to skyLayer (1)
        gameObjects().addGameObject(sky, skyLayer);
    }

    /**
     * main function, run the game
     * @param args getting no args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}
