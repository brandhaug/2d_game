package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class GameObject {

    private int x;
    private int y;
    private int velocityX = 0;
    private int velocityY = 0;
    private int currentSpriteState = 0;
    private int lastSpriteState = 0;
    private boolean rightCollision = false;
    private boolean leftCollision = false;
    private boolean firstCollision = false;
    private SpriteSheet currentSpriteSheet;

    /**
     * Sets the x and y position of the game object
     *
     * @param x the x position
     * @param y the y position
     */
    GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Handles the sprite state. Renders the Graphics Context.
     *
     * @param gc the Graphics Context.
     * @see GraphicsContext
     */
    public void tick(GraphicsContext gc) {
        handleSpriteStates();
        render(gc);
    }

    /**
     * Gets the x position
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x position
     *
     * @param x the x position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y position
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y position
     *
     * @param y the y position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the x velocity
     *
     * @return velocityX
     */
    public int getVelocityX() {
        return velocityX;
    }

    /**
     * Sets the x velocity. The max x velocity is 12, and 10000 if forced.
     *
     * @param inputVelocityX the velocity x
     * @param force          force max velocity
     */
    public void setVelocityX(int inputVelocityX, boolean force) {
        int MAX_VELOCITY_X;
        if (force) {
            MAX_VELOCITY_X = 10000;
        } else {
            MAX_VELOCITY_X = 12;
        }

        if (inputVelocityX >= MAX_VELOCITY_X) {
            velocityX = MAX_VELOCITY_X;
        } else if (inputVelocityX <= MAX_VELOCITY_X * -1) {
            velocityX = MAX_VELOCITY_X * -1;
        } else {
            velocityX = inputVelocityX;
        }
    }

    /**
     * Sets first collision to true
     */
    public void setFirstCollision() {
        firstCollision = true;
    }

    /**
     * Returns true if first collision is made, false if not.
     * @return firstCollision
     */
    public boolean getFirstCollision() {
        return firstCollision;
    }

    /**
     * Gets the y velocity
     *
     * @return velocityY
     */
    public int getVelocityY() {
        return velocityY;
    }

    /**
     * Sets the y velocity
     * @param velocityY the y velocity
     */
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Gets the current sprite sheet
     * @return currentSpriteSheet
     */
    SpriteSheet getCurrentSpriteSheet() {
        return currentSpriteSheet;
    }

    /**
     * Sets the current sprite sheet
     * @param currentSpriteSheet the current sprite sheet
     */
    void setCurrentSpriteSheet(SpriteSheet currentSpriteSheet) {
        this.currentSpriteSheet = currentSpriteSheet;
    }

    /**
     * Gets the current sprite state
     * @return currentSpriteState
     */
    public int getCurrentSpriteState() {
        return currentSpriteState;
    }

    /**
     * Sets the current sprite state
     * @param currentSpriteState the current sprite state
     */
    void setCurrentSpriteState(int currentSpriteState) {
        this.currentSpriteState = currentSpriteState;
    }

    /**
     * Gets the last sprite state
     * @return lastSpriteState
     */
    int getLastSpriteState() {
        return lastSpriteState;
    }

    /**
     * Sets the last sprite state
     * @param lastSpriteState the last sprite state
     */
    void setLastSpriteState(int lastSpriteState) {
        this.lastSpriteState = lastSpriteState;
    }

    /**
     * Abstract method to initialize sprite sheets
     */
    public abstract void initializeSpriteSheets();

    /**
     * Gets the bottom rectangle of a game object.
     * @see javafx.scene.shape.Rectangle
     * @return Rectangle
     */
    public Rectangle getBoundsBottom() {
        return new Rectangle(x, y + currentSpriteSheet.getSpriteHeight() - 10, currentSpriteSheet.getSpriteWidth(), 10);
    }

    /**
     * Gets the top rectangle of a game object.
     * @see javafx.scene.shape.Rectangle
     * @return Rectangle
     */
    public Rectangle getBoundsTop() {
        return new Rectangle(x, y, currentSpriteSheet.getSpriteWidth(), 10);
    }

    /**
     * Gets the right rectangle of a game object.
     * @see javafx.scene.shape.Rectangle
     * @return Rectangle
     */
    public Rectangle getBoundsRight() {
        return new Rectangle(x + currentSpriteSheet.getSpriteWidth() - 10, y + 10, 10, currentSpriteSheet.getSpriteHeight() - 20);
    }

    /**
     * Gets the left rectangle of a game object.
     * @see javafx.scene.shape.Rectangle
     * @return Rectangle
     */
    public Rectangle getBoundsLeft() {
        return new Rectangle(x, y + 10, 10, currentSpriteSheet.getSpriteHeight() - 20);
    }

    /**
     * Sets the last sprite state to the current sprite state
     */
    public void handleSpriteStates() {
        setLastSpriteState(currentSpriteState);
    }

    /**
     * Sets the right collision to parameters input
     * @param rightCollision true if right collision is made, false if not
     */
    public void setRightCollision(boolean rightCollision) {
        this.rightCollision = rightCollision;
    }

    /**
     * Sets the left collision to parameters input
     * @param leftCollision true if left collision is made, false if not
     */
    public void setLeftCollision(boolean leftCollision) {
        this.leftCollision = leftCollision;
    }

    /**
     * Renders the GraphicsContext
     * @param gc the GraphicsContext
     * @see GraphicsContext
     */
    public void render(GraphicsContext gc) {
        currentSpriteSheet.render(gc, x, y, currentSpriteState, lastSpriteState);
    }
}
