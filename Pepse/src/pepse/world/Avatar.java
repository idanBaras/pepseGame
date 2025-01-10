package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.renderable.RenderableImage;

/**
 * avatar class for the game
 */
public class Avatar extends GameObject {
    private static final String AVATAR_IMAGE = "idle_0.png";
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 600;
    private static final float JUMP_ENERGY = 10f;
    private static final float MAX_ENERGY = 100f;
    private static final float MOVEMENT_ENERGY = 0.5f;
    private static final float REGEN_ENERGY = 1f;
    private static final float AVATAR_DIMS = 50;
    private UserInputListener inputListener;
    private float energy = MAX_ENERGY;
    private AnimationRenderable idle;
    private AnimationRenderable move;
    private AnimationRenderable up;
    private boolean flip;
    private boolean jumping;
    private String[] idleClips = { "./assets/idle_0.png",
            "./assets/idle_1.png", "./assets/idle_2.png",
            "./assets/idle_3.png"};
    private String[] jumpClips = { "./assets/jump_0.png",
            "./assets/jump_1.png", "./assets/jump_2.png",
            "./assets/jump_3.png"};
    private String[] runClips = { "./assets/run_0.png","./assets/run_1.png",
            "./assets/run_2.png","./assets/run_3.png",
            "./assets/run_4.png","./assets/run_5.png"};
    private String PLAYER_TAG = "player";
    /**
     * avatar build func
     * @param topLeftCorner spawnPlace
     * @param inputListener input for avatar
     * @param imageReader imageReader to get avatar image
     */
    public Avatar(Vector2 topLeftCorner,
                   UserInputListener inputListener,
                   ImageReader imageReader) {
        super(topLeftCorner,new Vector2(AVATAR_DIMS,AVATAR_DIMS),
        imageReader.readImage
        ("./assets/idle_0.png",true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.idle = new AnimationRenderable(idleClips,imageReader,
                true,0.1);
        this.up = new AnimationRenderable(jumpClips,imageReader,
                true,0.1);
        this.move = new AnimationRenderable(runClips,imageReader,
                true,0.1);
        flip = false;
        jumping = false;
        this.setTag(PLAYER_TAG);
    }


    /**
     * update func for the input for avatar
     * @param deltaTime The time elapsed, in seconds,
     *                 since the last frame. Can
     * be used to determine a new position/velocity
     *                 by multiplying
     *this delta with the velocity/acceleration
     *                 respectively
     *and adding to the position/velocity:
     *velocity += deltaTime*acceleration
     *pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        boolean moved = false;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            xVel -= VELOCITY_X;}
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            xVel += VELOCITY_X;}
        if(xVel != 0 && energy>=MOVEMENT_ENERGY){
            energy -= MOVEMENT_ENERGY;
            transform().setVelocityX(xVel);
            moved = true;
        } else {
            transform().setVelocityX(0);}
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && energy>=JUMP_ENERGY){
            energy -= JUMP_ENERGY;
            transform().setVelocityY(VELOCITY_Y);
            moved = true;}
        if(getVelocity().y() == 0 && getVelocity().x() == 0
                && !moved){
            energy +=REGEN_ENERGY;
            if(energy>MAX_ENERGY){
                energy=MAX_ENERGY;}
            renderer().setRenderable(idle);
            renderer().setIsFlippedHorizontally(flip);}
        if(getVelocity().x() == 0 && getVelocity().y() != 0){
            renderer().setRenderable(up);
            renderer().setIsFlippedHorizontally(flip);
            jumping = true;}
        if(getVelocity().y() == 0 && jumping){
            jumping = false;}
        if(getVelocity().x() != 0){
            if(getVelocity().x()<0){
                flip = true;
            } else {
                flip = false;}
            renderer().setRenderable(move);
            renderer().setIsFlippedHorizontally(flip);}
    }

    /**
     * return cur energy
     * @return cur energy
     */
    public float getEnergy(){
        return energy;
    }

    /**
     * add energy to avatar
     * @param amount amount to add
     */
    public void addEnergy(float amount){
        if(energy + amount > MAX_ENERGY){
            energy=MAX_ENERGY;
        } else if(energy + amount < 0){
            energy = 0;
        } else {
            energy += amount;
        }
    }

    /**
     * return if the player is currently jumping
     * @return jumping var
     */
    public boolean isJumping(){
        return jumping;
    }

}
