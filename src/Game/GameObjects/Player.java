package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Player extends GameObject {

    private final int START_POSITION = 200;

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

    public Player(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
    }

    @Override
    public void setX(int x) {
        //TODO: Sette vegg helt til venstre i stedet?
        if (x >= START_POSITION) {
            super.setX(x);
        }
    }

    public int getStartPosition() {
        return START_POSITION;
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
        getCurrentSpriteSheet().draw(gc, START_POSITION, getY(), getCurrentSpriteState(), getLastSpriteState());
    }

    @Override
    public Rectangle getBoundsBottom() {
        return new Rectangle(START_POSITION + 10, getY() + getCurrentSpriteSheet().getSpriteHeight() / 2, getCurrentSpriteSheet().getSpriteWidth() - 20, getCurrentSpriteSheet().getSpriteHeight() / 2);
    }

    @Override
    public Rectangle getBoundsTop() {
        return new Rectangle(START_POSITION + 10, getY(), getCurrentSpriteSheet().getSpriteWidth() - 20, getCurrentSpriteSheet().getSpriteHeight() / 2);
    }

    @Override
    public Rectangle getBoundsRight() {
        return new Rectangle(START_POSITION + getCurrentSpriteSheet().getSpriteWidth() - 10, getY() + 10, 10, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle(START_POSITION, getY() + 10, 10, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    public void setRightCollision(boolean rightCollision) {
        this.rightCollision = rightCollision;
    }

    public void setLeftCollision(boolean leftCollision) {
        this.leftCollision = leftCollision;
    }
}
