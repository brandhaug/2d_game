package Game.GameObjects;

import Game.GameController;
import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Scanner;

public class Enemy extends Character{

    int hp;
    int speed = 1;
    int damage;
    int points;

    public static final int START_POSITION_X = 200;
    public static final int START_POSITION_Y = 400;
    private final int WIDTH = 72;

    // States
    public final static int ENEMY_IDLING_RIGHT = 0;
    public final static int ENEMY_IDLING_LEFT = 1;
    public final static int ENEMY_RUNNING_RIGHT = 2;
    public final static int ENEMY_RUNNING_LEFT = 3;
    public final static int ENEMY_JUMPING_RIGHT = 4;
    public final static int ENEMY_JUMPING_LEFT = 5;
    public final static int ENEMY_FALLING_RIGHT = 6;
    public final static int ENEMY_FALLING_LEFT = 7;



    // Spritesheets
    private SpriteSheet idleRightSpriteSheet;
    private SpriteSheet idleLeftSpriteSheet;
    private SpriteSheet runRightSpriteSheet;
    private SpriteSheet runLeftSpriteSheet;
    private SpriteSheet jumpLeftSpriteSheet;
    private SpriteSheet fallLeftSpriteSheet;
    private SpriteSheet jumpRightSpriteSheet;
    private SpriteSheet fallRightSpriteSheet;

    private boolean rightCollision = false;
    private boolean leftCollision = false;
    private boolean lastSpriteRight = true;
    private boolean isAlive = true;


    EnemyType enemyType;

    /*
    public Enemy(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
    }
    */

    public Enemy(int x, int y, EnemyType enemyType) {
        super(x, y);
        this.enemyType = enemyType;
        setVelocityY(10);
        setVelocityX(0,true);
        initializeSpriteSheets();
    }


    public Enemy(int x, int y) {
        super(x, y);
        this.enemyType = enemyType;
        initializeSpriteSheets();
    }

    /*
    public enum enemyType {
        IMMORTAL, EASY1, EASY2, EASY3,
        MEDIUM1, MEDIUM2, MEDIUM3,
        HARD1, HARD2, HARD3
    }
    */

    public void setHp(int hp) {
    }

    public int getHp() {
        return 0;
    }

    public void setAlive(boolean isAlive) {

    }

    public boolean getAlive() {
        return false;
    }

    public int getStartPosition() {
        return 0;
    }

    public boolean getLastSpriteRight() {
        return false;
    }

    public void tick(GraphicsContext gc) {
        //setVelocityY(1);
        handleVelocityX();
        handleVelocityY();
        render(gc);
    }

    @Override
    public void handleSpriteState() {
        setLastSpriteState(getCurrentSpriteState());

        if (getVelocityX() == 0 && getVelocityY() == 0 && (!leftCollision && !rightCollision)) {
            if (lastSpriteRight) {
                lastSpriteRight = true;
                setCurrentSpriteState(ENEMY_IDLING_RIGHT);
                setCurrentSpriteSheet(idleRightSpriteSheet);
            } else {
                lastSpriteRight = false;
                setCurrentSpriteState(ENEMY_IDLING_LEFT);
                setCurrentSpriteSheet(idleLeftSpriteSheet);
            }
        } else if (getVelocityY() < 0 && (getVelocityX() > 0 || getVelocityX() == 0 && lastSpriteRight)) {
            lastSpriteRight = true;
            setCurrentSpriteState(ENEMY_JUMPING_RIGHT);
            setCurrentSpriteSheet(jumpRightSpriteSheet);
        } else if (getVelocityY() < 0 && getVelocityX() <= 0) {
            lastSpriteRight = false;
            setCurrentSpriteState(ENEMY_JUMPING_LEFT);
            setCurrentSpriteSheet(jumpLeftSpriteSheet);
        } else if (getVelocityY() > 0 && (getVelocityX() > 0 || getVelocityX() == 0 && lastSpriteRight)) {
            lastSpriteRight = true;
            setCurrentSpriteState(ENEMY_FALLING_RIGHT);
            setCurrentSpriteSheet(fallRightSpriteSheet);
        } else if (getVelocityY() > 0 && getVelocityX() <= 0) {
            lastSpriteRight = false;
            setCurrentSpriteState(ENEMY_FALLING_LEFT);
            setCurrentSpriteSheet(fallLeftSpriteSheet);
        } else if (getVelocityX() > 0 || rightCollision) {
            lastSpriteRight = true;
            setCurrentSpriteState(ENEMY_RUNNING_RIGHT);
            setCurrentSpriteSheet(runRightSpriteSheet);
        } else if (getVelocityX() < 0 || leftCollision) {
            lastSpriteRight = false;
            setCurrentSpriteState(ENEMY_RUNNING_LEFT);
            setCurrentSpriteSheet(runLeftSpriteSheet);
        }
    }

    public void initializeSpriteSheets() {
        idleRightSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName()+ "/idle_right.png", 12, 72, 76);
        idleLeftSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName()+ "/idle_left.png" , 12, 72, 76);
        runRightSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName()+ "/run_right.png", 18, 99, 77);
        runLeftSpriteSheet = new SpriteSheet("/Resources/"  + enemyType.getFileName()+ "/run_left.png", 18, 99, 77);
        jumpRightSpriteSheet = new SpriteSheet("/Resources/"  + enemyType.getFileName()+ "/jump_right.png", 1, 76, 77);
        jumpLeftSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName()+ "/jump_left.png", 1, 76, 77);
        fallRightSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName()+ "/fall_left.png", 1, 67, 77);
        fallLeftSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName()+ "/fall_right.png", 1, 67, 77);
    }

    public void setRightCollision(boolean rightCollision) {
        this.rightCollision = rightCollision;
    }

    public void setLeftCollision(boolean leftCollision) {
        this.leftCollision = leftCollision;
    }

    @Override
    public void render(GraphicsContext gc) {
        getCurrentSpriteSheet().render(gc, getX(), getY(), getCurrentSpriteState(), getLastSpriteState());

        // Draw bounds
        gc.setFill(Color.BLACK);
        gc.strokeRect(getX() + 20, getY() + getCurrentSpriteSheet().getSpriteHeight() - 20, WIDTH - 40, 20);
        gc.strokeRect(getX() + 20, getY(), WIDTH - 40, 20);
        gc.strokeRect(getX() + WIDTH - 20, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
        gc.strokeRect(getX(), getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    @Override
    public Rectangle getBoundsBottom() {
        return new Rectangle(getX(), getY(), 40, 72);
    }

    @Override
    public Rectangle getBoundsTop() {
        return new Rectangle(getX()+ 20, getY(), 40, 20);
    }

    @Override
    public Rectangle getBoundsRight() {
        return new Rectangle(getX() + WIDTH - 20, getY() + 10, 20, 20);
    }

    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle(getX(), getY() + 10, 20,  40);
    }


}
