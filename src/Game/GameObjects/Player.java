package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Player extends GameObject {

    private final int START_POSITION = 200;

    // States
    public final static int PLAYER_IDLING = 0;
    public final static int PLAYER_RUNNING_RIGHT = 1;
    public final static int PLAYER_RUNNING_LEFT = 2;
    public final static int PLAYER_JUMPING = 3;
    public final static int PLAYER_FALLING = 4;

    // Spritesheets
    private SpriteSheet idleSpriteSheet;
    private SpriteSheet rightRunSpriteSheet;
    private SpriteSheet leftRunSpriteSheet;
    private SpriteSheet jumpSpriteSheet;

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
        setLastSpriteState(getCurrentSpriteState());
        setCurrentSpriteState();
        handleVelocityX();
        handleVelocityY();
        render(gc);
    }

    private void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    private void handleVelocityY() {
        int MAX_VELOCITY_Y = 20;

        if (getVelocityY() >= MAX_VELOCITY_Y) {
            setY(getY() + MAX_VELOCITY_Y);
        } else {
            setY(getY() + getVelocityY());
        }
    }

    private void setCurrentSpriteState() {
        if (getVelocityY() < 0) {
            setCurrentSpriteState(PLAYER_JUMPING);
            setCurrentSpriteSheet(jumpSpriteSheet);
        } else if (getVelocityY() > 0) {
            setCurrentSpriteState(PLAYER_FALLING);
            setCurrentSpriteSheet(jumpSpriteSheet);
        } else if (getVelocityY() > 0) {
            setCurrentSpriteState(PLAYER_RUNNING_RIGHT);
            setCurrentSpriteSheet(rightRunSpriteSheet);
        } else if (getVelocityY() < 0) {
            setCurrentSpriteState(PLAYER_RUNNING_LEFT);
            setCurrentSpriteSheet(leftRunSpriteSheet);
        } else {
            setCurrentSpriteState(PLAYER_IDLING);
            setCurrentSpriteSheet(idleSpriteSheet);
        }
    }

    @Override
    public void initializeSpriteSheets() {
        idleSpriteSheet = new SpriteSheet("/Resources/player/idle.png", 12, 72, 76);
        rightRunSpriteSheet = new SpriteSheet("/Resources/player/run_right.png", 18, 99, 77);
        leftRunSpriteSheet = new SpriteSheet("/Resources/player/run_left.png", 18, 99, 77);
        jumpSpriteSheet = new SpriteSheet("/Resources/player/jump.png", 2, 83, 78);
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
        return new Rectangle(START_POSITION + getCurrentSpriteSheet().getSpriteWidth() - 5, getY() + 5, 5, getCurrentSpriteSheet().getSpriteHeight() - 10);
    }

    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle(START_POSITION, getY() + 5, 5, getCurrentSpriteSheet().getSpriteHeight() - 10);
    }
}
