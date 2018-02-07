package Game.GameObjects;

import Game.GameController;
import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

public class Player implements GameObject {

    private int x;
    private int y;
    private int velocityX = 0;
    private int velocityY = 10;
    public static int relativeX = 128;
    private final int MAX_VELOCITY_X = 15;
    public final static int START_POSITION = 200;

    private int PLAYER_IDLING = 0;
    private int PLAYER_RUNNING_RIGHT = 1;
    private int PLAYER_RUNNING_LEFT = 2;
    private int PLAYER_JUMPING = 3;
    private int currentSpriteState = 0;
    private int lastSpriteState = 0;

    private SpriteSheet idleSpriteSheet;
    private SpriteSheet rightRunSpriteSheet;
    private SpriteSheet jumpSpriteSheet;

    private final String idleSpriteSheetPath = "/Resources/player/idle.png";
    private final int idleSpriteWidth = 144;
    private final int idleSpriteHeight = 152;
    private final int idleSpriteSheetCols = 12;

    private final String rightRunSpriteSheetPath = "/Resources/player/run.png";
    private final int rightRunSpriteWidth = 198;
    private final int rightRunSpriteHeight = 152;
    private final int rightRunSpriteSheetCols = 18;

    private final String jumpSpriteSheetPath = "/Resources/player/jump.png";
    private final int jumpSpriteWidth = 167;
    private final int jumpSpriteHeight = 155;
    private final int jumpSpriteSheetCols = 2;

    public Player(GraphicsContext gc) {
        initializeSpriteSheets();
        tick(gc);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public void draw(GraphicsContext gc) {
        if (currentSpriteState == PLAYER_IDLING) {
            idleSpriteSheet.draw(gc, START_POSITION, y, currentSpriteState, lastSpriteState);
        } else if (currentSpriteState == PLAYER_RUNNING_RIGHT || currentSpriteState == PLAYER_RUNNING_LEFT) {
            rightRunSpriteSheet.draw(gc, START_POSITION, y, currentSpriteState, lastSpriteState);
        } else if (currentSpriteState == PLAYER_JUMPING) {
            jumpSpriteSheet.draw(gc, START_POSITION, y, currentSpriteState, lastSpriteState);
        }
    }

    public void tick(GraphicsContext gc) {
        lastSpriteState = currentSpriteState;

        if (velocityX >= MAX_VELOCITY_X) {
            x += MAX_VELOCITY_X;
        } else if (velocityX <= MAX_VELOCITY_X * -1) {
            x -= MAX_VELOCITY_X;
        } else {
            x += velocityX;
        }

        if (velocityY != 0) {
            currentSpriteState = PLAYER_JUMPING;
        } else if (velocityX > 0) {
            currentSpriteState = PLAYER_RUNNING_RIGHT;
        } else if (velocityX < 0) {
            currentSpriteState = PLAYER_RUNNING_LEFT;
        } else {
            currentSpriteState = PLAYER_IDLING;
        }


        y += velocityY;

        if (y > GameController.HEIGHT - rightRunSpriteHeight - 100) {
            velocityY = 0;
        } else {
            velocityY += 5;
        }

        draw(gc);
    }

    private void initializeSpriteSheets() {
        idleSpriteSheet = new SpriteSheet(idleSpriteSheetPath, idleSpriteSheetCols, idleSpriteWidth, idleSpriteHeight);
        rightRunSpriteSheet = new SpriteSheet(rightRunSpriteSheetPath, rightRunSpriteSheetCols, rightRunSpriteWidth, rightRunSpriteHeight);
        jumpSpriteSheet = new SpriteSheet(jumpSpriteSheetPath, jumpSpriteSheetCols, jumpSpriteWidth, jumpSpriteHeight);
    }
}
