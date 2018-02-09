package Game.GameObjects;

import Game.Levels.Beginner;
import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Player extends GameObject {

    private GraphicsContext gc;

    // Position and Velocity
    private int x;
    private int y;
    private int velocityX = 0;
    private int velocityY = 5;

    private final int START_POSITION = 200;

    // States
    public final static int PLAYER_IDLING = 0;
    public final static int PLAYER_RUNNING_RIGHT = 1;
    public final static int PLAYER_RUNNING_LEFT = 2;
    public final static int PLAYER_JUMPING = 3;
    public final static int PLAYER_FALLING = 4;

    private int currentSpriteState = 0;
    private int lastSpriteState = 0;

    // Spritesheets
    private SpriteSheet idleSpriteSheet;
    private SpriteSheet rightRunSpriteSheet;
    private SpriteSheet leftRunSpriteSheet;
    private SpriteSheet jumpSpriteSheet;
    private SpriteSheet currentSpriteSheet;

    public Player(GraphicsContext gc) {
        this.gc = gc;
        this.x = START_POSITION;
        initializeSpriteSheets();
        tick();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        if (x >= START_POSITION) {
            this.x = x;
        }
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    @Override
    public int getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public int getStartPosition() {
        return START_POSITION;
    }

    public int getCurrentSpriteState() {
        return currentSpriteState;
    }

    public SpriteSheet getCurrentSpriteSheet() {
        return currentSpriteSheet;
    }

    public void tick() {
        lastSpriteState = currentSpriteState;
        setCurrentSpriteState();
        handleVelocityX();
        handleVelocityY();
        render(gc);
    }

    private void handleVelocityX() {
        int MAX_VELOCITY_X = 12;

        if (velocityX >= MAX_VELOCITY_X) {
            setX(getX() + MAX_VELOCITY_X);
        } else if (velocityX <= MAX_VELOCITY_X * -1) {
            setX(getX() - MAX_VELOCITY_X);
        } else {
            setX(getX() + velocityX);
        }
    }

    private void handleVelocityY() {
        int MAX_VELOCITY_Y = 20;

        if (velocityY >= MAX_VELOCITY_Y) {
            setY(getY() + MAX_VELOCITY_Y);
        } else {
            setY(getY() + velocityY);
        }
    }

    private void setCurrentSpriteState() {
        if (velocityY < 0) {
            currentSpriteState = PLAYER_JUMPING;
            currentSpriteSheet = jumpSpriteSheet;
        } else if (velocityY > 0) {
            currentSpriteState = PLAYER_FALLING;
            currentSpriteSheet = jumpSpriteSheet;
        } else if (velocityX > 0) {
            currentSpriteState = PLAYER_RUNNING_RIGHT;
            currentSpriteSheet = rightRunSpriteSheet;
        } else if (velocityX < 0) {
            currentSpriteState = PLAYER_RUNNING_LEFT;
            currentSpriteSheet = leftRunSpriteSheet;
        } else {
            currentSpriteState = PLAYER_IDLING;
            currentSpriteSheet = idleSpriteSheet;
        }
    }

    private void initializeSpriteSheets() {
        idleSpriteSheet = new SpriteSheet("/Resources/player/idle.png", 12, 72, 76);
        rightRunSpriteSheet = new SpriteSheet("/Resources/player/run_right.png", 18, 99, 77);
        leftRunSpriteSheet = new SpriteSheet("/Resources/player/run_left.png", 18, 99, 77);
        jumpSpriteSheet = new SpriteSheet("/Resources/player/jump.png", 2, 83, 78);
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle(x + 10, y + currentSpriteSheet.getSpriteHeight() / 2, currentSpriteSheet.getSpriteWidth() - 20, currentSpriteSheet.getSpriteHeight() / 2);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(x + 10, y, currentSpriteSheet.getSpriteWidth() - 20, currentSpriteSheet.getSpriteHeight() / 2);
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(x + currentSpriteSheet.getSpriteWidth() - 5, y + 5, 5, currentSpriteSheet.getSpriteHeight() - 10);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(x, y + 5, 5, currentSpriteSheet.getSpriteHeight() - 10);
    }

    private void render(GraphicsContext gc) {
        currentSpriteSheet.draw(gc, START_POSITION, y, currentSpriteState, lastSpriteState);
    }

}
