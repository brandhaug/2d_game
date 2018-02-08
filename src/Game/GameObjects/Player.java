package Game.GameObjects;

import Game.GameController;
import Game.Levels.Beginner;
import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Player implements GameObject {

    private int x;
    private int y;
    private int velocityX = 0;
    private int velocityY = 5;
    private final int MAX_VELOCITY_X = 12;
    private final int MAX_VELOCITY_Y = 20;
    private final int START_POSITION = 200;

    private int PLAYER_IDLING = 0;
    private int PLAYER_RUNNING_RIGHT = 1;
    private int PLAYER_RUNNING_LEFT = 2;
    private int PLAYER_JUMPING = 3;
    private int PLAYER_FALLING = 4;

    private int currentSpriteState = 0;
    private int lastSpriteState = 0;

    private SpriteSheet idleSpriteSheet;
    private SpriteSheet rightRunSpriteSheet;
    private SpriteSheet leftRunSpriteSheet;
    private SpriteSheet jumpSpriteSheet;
    private SpriteSheet currentSpriteSheet;

    public Player(GraphicsContext gc) {
        this.x = START_POSITION;
        initializeSpriteSheets();
        tick(gc);
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

    private void draw(GraphicsContext gc) {
        if (currentSpriteState == PLAYER_IDLING) {
            idleSpriteSheet.draw(gc, START_POSITION, y, currentSpriteState, lastSpriteState);
        } else if (currentSpriteState == PLAYER_RUNNING_RIGHT) {
            rightRunSpriteSheet.draw(gc, START_POSITION, y, currentSpriteState, lastSpriteState);
        } else if (currentSpriteState == PLAYER_RUNNING_LEFT) {
            leftRunSpriteSheet.draw(gc, START_POSITION, y, currentSpriteState, lastSpriteState);
        } else if (currentSpriteState == PLAYER_JUMPING || currentSpriteState == PLAYER_FALLING) {
            jumpSpriteSheet.draw(gc, START_POSITION, y, currentSpriteState, lastSpriteState);
        }
    }

    public void tick(GraphicsContext gc) {
        lastSpriteState = currentSpriteState;
        setCurrentSpriteState();
        handleVelocityX();
        handleVelocityY();
        draw(gc);
    }

    private void handleVelocityX() {
        if (velocityX >= MAX_VELOCITY_X) {
            setX(getX() + MAX_VELOCITY_X);
        } else if (velocityX <= MAX_VELOCITY_X * -1) {
            setX(getX() - MAX_VELOCITY_X);
        } else {
            setX(getX() + velocityX);
        }
    }

    private void handleVelocityY() {
        collision();

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

    public void collision() {
        boolean intersected = false;
        for (Tile tile : Beginner.getTiles()) {
            if (this.getBoundsBottom().intersects(tile.getBoundsTop()) && currentSpriteState != PLAYER_JUMPING) {
                intersected = true;
                this.setY(tile.getY() - this.currentSpriteSheet.getSpriteHeight() + 10);
                break;
            }
        }

        if (intersected) {
            velocityY = 0;
        } else {
            velocityY += 2;
        }
    }
}
