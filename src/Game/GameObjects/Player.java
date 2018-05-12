package Game.GameObjects;

import Game.GameController;
import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Player extends GameObject {

    private final int WIDTH = 72;

    /**
     * States
     */
    public final static int PLAYER_IDLING_RIGHT = 0;
    private final static int PLAYER_IDLING_LEFT = 1;
    public final static int PLAYER_RUNNING_RIGHT = 2;
    private final static int PLAYER_RUNNING_LEFT = 3;
    public final static int PLAYER_JUMPING_RIGHT = 4;
    private final static int PLAYER_JUMPING_LEFT = 5;
    public final static int PLAYER_FALLING_RIGHT = 6;
    private final static int PLAYER_FALLING_LEFT = 7;
    private int hp = 100; //Hit Points

    /**
     * State monitors
     */
    private boolean lastSpriteRight = true;
    private boolean rightCollision = false;
    private boolean leftCollision = false;
    private boolean isAlive = true;
    private String playerType;

    /**
     * Spritesheets
     */
    private SpriteSheet idleRightSpriteSheet;
    private SpriteSheet idleLeftSpriteSheet;
    private SpriteSheet runRightSpriteSheet;
    private SpriteSheet runLeftSpriteSheet;
    private SpriteSheet jumpLeftSpriteSheet;
    private SpriteSheet fallLeftSpriteSheet;
    private SpriteSheet jumpRightSpriteSheet;
    private SpriteSheet fallRightSpriteSheet;

    /**
     * Sets parameters.
     * Sets velocity x correct according to where player spawns.
     * Sets velocity y to 1.
     * Initializes sprite sheets.
     * @param x the x position
     * @param y the y position
     * @param playerType the player type
     */
    public Player(int x, int y, String playerType) {
        super(x, y);
        this.playerType = playerType;
        setVelocityX(x - GameController.PLAYER_X_MARGIN, true);
        setVelocityY(1);
        initializeSpriteSheets();
    }

    /**
     * Returns 0
     * @return 0
     */
    public int getStartPosition() {
        return 0;
    }

    /**
     * Gets the last sprite right
     * @return lastSpriteRight
     */
    public boolean getLastSpriteRight() {
        return lastSpriteRight;
    }

    /**
     * Handles x and y velocity. Renders the GraphicsContext
     * @see GraphicsContext
     * @param gc the Graphics Context.
     */
    public void tick(GraphicsContext gc) {
        handleVelocityX();
        handleVelocityY();
        render(gc);
    }

    /**
     * Sets the hp of the player
     * @param hp the hp
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Gets the hp of the player
     * @return hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * Sets true if player is alive, false if dead
     * @param isAlive
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Returns true if player is alive, false if dead
     * @return isAlive
     */
    public boolean getAlive() {
        return isAlive;
    }

    /**
     * Sets the x position according to velocity X
     */
    private void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    /**
     * Sets the y position of the player according to the velocity y of the player
     * The player will never fall get a higher velocity then 13 when falling and never lower then -35 when falling.
     */
    private void handleVelocityY() {
        int MAX_VELOCITY_FALLING = 13;
        int MAX_VELOCITY_JUMPING = -35;

        if (getVelocityY() >= MAX_VELOCITY_FALLING) {
            setY(getY() + MAX_VELOCITY_FALLING);
        } else if (getY() <= MAX_VELOCITY_JUMPING) {
            setY(getY() + MAX_VELOCITY_JUMPING);
        } else {
            setY(getY() + getVelocityY());
        }
    }

    /**
     * Sets x only if x is larger than 200
     * @param x the x position
     */
    @Override
    public void setX(int x) {
        if (x > 200) {
            super.setX(x);
        }
    }

    /**
     * Updates current and last sprite states according to x and y velocity.
     */
    @Override
    public void handleSpriteStates() {
        setLastSpriteState(getCurrentSpriteState());

        if (getVelocityX() == 0 && getVelocityY() == 0 && (!leftCollision && !rightCollision)) {
            if (lastSpriteRight) {
                lastSpriteRight = true;
                setCurrentSpriteState(PLAYER_IDLING_RIGHT);
                setCurrentSpriteSheet(idleRightSpriteSheet);
            } else {
                lastSpriteRight = false;
                setCurrentSpriteState(PLAYER_IDLING_LEFT);
                setCurrentSpriteSheet(idleLeftSpriteSheet);
            }
        } else if (getVelocityY() < 0 && (getVelocityX() > 0 || getVelocityX() == 0 && lastSpriteRight)) {
            lastSpriteRight = true;
            setCurrentSpriteState(PLAYER_JUMPING_RIGHT);
            setCurrentSpriteSheet(jumpRightSpriteSheet);
        } else if (getVelocityY() < 0 && getVelocityX() <= 0) {
            lastSpriteRight = false;
            setCurrentSpriteState(PLAYER_JUMPING_LEFT);
            setCurrentSpriteSheet(jumpLeftSpriteSheet);
        } else if (getVelocityY() > 0 && (getVelocityX() > 0 || getVelocityX() == 0 && lastSpriteRight)) {
            lastSpriteRight = true;
            setCurrentSpriteState(PLAYER_FALLING_RIGHT);
            setCurrentSpriteSheet(fallRightSpriteSheet);
        } else if (getVelocityY() > 0 && getVelocityX() <= 0) {
            lastSpriteRight = false;
            setCurrentSpriteState(PLAYER_FALLING_LEFT);
            setCurrentSpriteSheet(fallLeftSpriteSheet);
        } else if (getVelocityX() > 0 || rightCollision) {
            lastSpriteRight = true;
            setCurrentSpriteState(PLAYER_RUNNING_RIGHT);
            setCurrentSpriteSheet(runRightSpriteSheet);
        } else if (getVelocityX() < 0 || leftCollision) {
            lastSpriteRight = false;
            setCurrentSpriteState(PLAYER_RUNNING_LEFT);
            setCurrentSpriteSheet(runLeftSpriteSheet);
        }
    }

    /**
     * Initializes the sprite sheets for animation purposes.
     */
    public void initializeSpriteSheets() {
        idleRightSpriteSheet = new SpriteSheet("/Resources/player/" + playerType + "/player_" + playerType +"_idleRight.png", 12, 72, 76);
        idleLeftSpriteSheet = new SpriteSheet("/Resources/player/" + playerType + "/player_" + playerType +"_idleLeft.png", 12, 72, 76);
        runRightSpriteSheet = new SpriteSheet("/Resources/player/" + playerType + "/player_" + playerType +"_runRight.png", 18, 99, 77);
        runLeftSpriteSheet = new SpriteSheet("/Resources/player/" + playerType + "/player_" + playerType +"_runLeft.png", 18, 99, 77);
        jumpRightSpriteSheet = new SpriteSheet("/Resources/player/" + playerType + "/player_" + playerType +"_jumpRight.png", 1, 76, 77);
        jumpLeftSpriteSheet = new SpriteSheet("/Resources/player/" + playerType + "/player_" + playerType +"_jumpLeft.png", 1, 76, 77);
        fallRightSpriteSheet = new SpriteSheet("/Resources/player/" + playerType + "/player_" + playerType +"_fallRight.png", 1, 67, 77);
        fallLeftSpriteSheet = new SpriteSheet("/Resources/player/" + playerType + "/player_" + playerType +"_fallLeft.png", 1, 67, 77);
    }

    /**
     * Renders the GraphicsContext
     * @param gc the GraphicsContext
     */
    @Override
    public void render(GraphicsContext gc) {
        getCurrentSpriteSheet().render(gc, GameController.PLAYER_X_MARGIN, getY(), getCurrentSpriteState(), getLastSpriteState());
    }

    /**
     * Gets the bottom rectangle of a game object.
     * @see javafx.scene.shape.Rectangle
     * @return Rectangle
     */
    @Override
    public Rectangle getBoundsBottom() {
        return new Rectangle(GameController.PLAYER_X_MARGIN + 20, getY() + getCurrentSpriteSheet().getSpriteHeight() - 20, WIDTH - 40, 20);
    }

    /**
     * Gets the top rectangle of a game object.
     * @see javafx.scene.shape.Rectangle
     * @return Rectangle
     */
    @Override
    public Rectangle getBoundsTop() {
        return new Rectangle(GameController.PLAYER_X_MARGIN + 20, getY(), WIDTH - 40, 20);
    }

    /**
     * Gets the right rectangle of a game object.
     * @see javafx.scene.shape.Rectangle
     * @return Rectangle
     */
    @Override
    public Rectangle getBoundsRight() {
        return new Rectangle(GameController.PLAYER_X_MARGIN + WIDTH - 20, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    /**
     * Gets the left rectangle of a game object.
     * @see javafx.scene.shape.Rectangle
     * @return Rectangle
     */
    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle(GameController.PLAYER_X_MARGIN, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
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
     * Returns true if player is falling.
     * @return true if player is falling
     */
    public boolean isFalling() {
        return getCurrentSpriteState() == Player.PLAYER_FALLING_RIGHT || getCurrentSpriteState() == Player.PLAYER_FALLING_LEFT;
    }

    /**
     * Returns true if player is jumping.
     * @return true if player is jumping
     */
    public boolean isJumping() {
        return getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT || getCurrentSpriteState() == Player.PLAYER_JUMPING_LEFT;
    }

    /**
     * Returns true if player is idling.
     * @return true if player is idling
     */
    public boolean isIdling() {
        return getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT || getCurrentSpriteState() == Player.PLAYER_IDLING_LEFT;
    }

    /**
     * Returns true if player is running.
     * @return true if player is running
     */
    public boolean isRunning() {
        return getCurrentSpriteState() == Player.PLAYER_RUNNING_RIGHT || getCurrentSpriteState() == Player.PLAYER_RUNNING_LEFT;
    }
}
