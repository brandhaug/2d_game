package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Player extends GameObject {

    public static final int START_POSITION_X = 200;
    public static final int START_POSITION_Y = 500;
    private final int WIDTH = 72;

    // States
    public final static int PLAYER_IDLING_RIGHT = 0;
    public final static int PLAYER_IDLING_LEFT = 1;
    public final static int PLAYER_RUNNING_RIGHT = 2;
    public final static int PLAYER_RUNNING_LEFT = 3;
    public final static int PLAYER_JUMPING_RIGHT = 4;
    public final static int PLAYER_JUMPING_LEFT = 5;
    public final static int PLAYER_FALLING_RIGHT = 6;
    public final static int PLAYER_FALLING_LEFT = 7;

    // Spritesheets
    private SpriteSheet idleRightSpriteSheet;
    private SpriteSheet idleLeftSpriteSheet;
    private SpriteSheet runRightSpriteSheet;
    private SpriteSheet runLeftSpriteSheet;
    private SpriteSheet jumpLeftSpriteSheet;
    private SpriteSheet fallLeftSpriteSheet;
    private SpriteSheet jumpRightSpriteSheet;
    private SpriteSheet fallRightSpriteSheet;

    private boolean lastSpriteRight = true;
    private boolean rightCollision = false;
    private boolean leftCollision = false;

    private int hp = 100; //Hit Points


    private int lastY;

    public int getLastY() {
        return lastY;
    }

    public Player(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
        lastY = y;
    }

    @Override
    public void setX(int x) {
        //TODO: Sette vegg helt til venstre i stedet?
        if (x >= START_POSITION_X) {
            super.setX(x);
        }
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public int getStartPosition() {
        return START_POSITION_X;
    }

    public boolean getLastSpriteRight(){
        return lastSpriteRight;
    }

    public void tick(GraphicsContext gc) {
        handleVelocityX();
        handleVelocityY();
        render(gc);
    }

    private void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    private void handleVelocityY() {
        int MAX_VELOCITY_FALLING = 13;
        int MAX_VELOCITY_JUMPING = -35;

        lastY = getY();

        if (getVelocityY() >= MAX_VELOCITY_FALLING) {
            setY(getY() + MAX_VELOCITY_FALLING);
        } else if (getY() <= MAX_VELOCITY_JUMPING) {
            setY(getY() + MAX_VELOCITY_JUMPING);
        } else {
            setY(getY() + getVelocityY());
        }
    }

    @Override
    public void handleSpriteState() {
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

    @Override
    public void initializeSpriteSheets() {
        idleRightSpriteSheet = new SpriteSheet("/Resources/player/idle_right.png", 12, 72, 76);
        idleLeftSpriteSheet = new SpriteSheet("/Resources/player/idle_left.png", 12, 72, 76);
        runRightSpriteSheet = new SpriteSheet("/Resources/player/run_right.png", 18, 99, 77);
        runLeftSpriteSheet = new SpriteSheet("/Resources/player/run_left.png", 18, 99, 77);
        jumpRightSpriteSheet = new SpriteSheet("/Resources/player/jump_right.png", 1, 76, 77);
        jumpLeftSpriteSheet = new SpriteSheet("/Resources/player/jump_left.png", 1, 76, 77);
        fallRightSpriteSheet = new SpriteSheet("/Resources/player/fall_right.png", 1, 67, 77);
        fallLeftSpriteSheet = new SpriteSheet("/Resources/player/fall_left.png", 1, 67, 77);
    }

    @Override
    public void render(GraphicsContext gc) {
        getCurrentSpriteSheet().render(gc, START_POSITION_X, getY(), getCurrentSpriteState(), getLastSpriteState());

        // Draw bounds
        gc.setFill(Color.BLACK);
        gc.strokeRect(START_POSITION_X + 20, getY() + getCurrentSpriteSheet().getSpriteHeight() - 20, WIDTH - 40, 20);
        gc.strokeRect(START_POSITION_X + 20, getY(), WIDTH - 40, 20);
        gc.strokeRect(START_POSITION_X + WIDTH - 20, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
        gc.strokeRect(START_POSITION_X, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    @Override
    public Rectangle getBoundsBottom() {
        return new Rectangle(START_POSITION_X + 20, getY() + getCurrentSpriteSheet().getSpriteHeight() - 20, WIDTH - 40, 20);
    }

    @Override
    public Rectangle getBoundsTop() {
        return new Rectangle(START_POSITION_X + 20, getY(), WIDTH - 40, 20);
    }

    @Override
    public Rectangle getBoundsRight() {
        return new Rectangle(START_POSITION_X + WIDTH - 20, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle(START_POSITION_X, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    public void setRightCollision(boolean rightCollision) {
        this.rightCollision = rightCollision;
    }

    public void setLeftCollision(boolean leftCollision) {
        this.leftCollision = leftCollision;
    }

    public boolean isFalling() {
        return getCurrentSpriteState() == Player.PLAYER_FALLING_RIGHT || getCurrentSpriteState() == Player.PLAYER_FALLING_LEFT;
    }

    public boolean isJumping() {
        return getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT || getCurrentSpriteState() == Player.PLAYER_JUMPING_LEFT;
    }

    public boolean isIdling() {
        return getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT || getCurrentSpriteState() == Player.PLAYER_IDLING_LEFT;
    }

    public boolean isRunning() {
        return getCurrentSpriteState() == Player.PLAYER_RUNNING_RIGHT || getCurrentSpriteState() == Player.PLAYER_RUNNING_LEFT;
    }
}
