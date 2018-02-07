package Game.GameObjects;

import Game.GameController;
import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

public class Player implements GameObject {

    private int x;
    private int y;
    private int velocityX = 0;
    private int velocityY = 10;
    private final int MAX_VELOCITY_X = 12;
    private final int START_POSITION = 200;

    private int PLAYER_IDLING = 0;
    private int PLAYER_RUNNING_RIGHT = 1;
    private int PLAYER_RUNNING_LEFT = 2;
    private int PLAYER_JUMPING = 3;
    private int currentSpriteState = 0;
    private int lastSpriteState = 0;

    private SpriteSheet idleSpriteSheet;
    private SpriteSheet rightRunSpriteSheet;
    private SpriteSheet leftRunSpriteSheet;
    private SpriteSheet jumpSpriteSheet;

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
        } else if (currentSpriteState == PLAYER_JUMPING) {
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
        setY(getY() + velocityY);
        if (getY() > GameController.CANVAS_HEIGHT - 152 - 100) {
            velocityY = 0;
        } else {
            velocityY += 2;
        }
    }

    private void setCurrentSpriteState() {
        if (velocityY != 0) {
            currentSpriteState = PLAYER_JUMPING;
        } else if (velocityX > 0) {
            currentSpriteState = PLAYER_RUNNING_RIGHT;
        } else if (velocityX < 0) {
            currentSpriteState = PLAYER_RUNNING_LEFT;
        } else {
            currentSpriteState = PLAYER_IDLING;
        }
    }

    private void initializeSpriteSheets() {
        idleSpriteSheet = new SpriteSheet("/Resources/player/idle.png", 12, 72, 76);
        rightRunSpriteSheet = new SpriteSheet("/Resources/player/run_right.png", 18, 99, 77);
        leftRunSpriteSheet = new SpriteSheet("/Resources/player/run_left.png", 18, 99, 77);
        jumpSpriteSheet = new SpriteSheet("/Resources/player/jump.png", 2, 83, 78);
    }
}
