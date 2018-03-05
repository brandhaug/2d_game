package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.*;

public class Enemy extends GameObject{

    private int hp;
    private int speed;
    private int damage;
    private int points;
    private boolean enemyhit = false;

    private boolean rightCollision = false;
    private boolean leftCollision = false;
    public boolean lastSpriteRight = true;
    private boolean isAlive = true;
    EnemyType enemyType;

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
    public final static int ENEMY_HIT_RIGHT = 8;
    public final static int ENEMY_HIT_LEFT = 9;
    public final static int ENEMY_DEAD = 10;

    // Spritesheets
    private SpriteSheet idleRightSpriteSheet;
    private SpriteSheet idleLeftSpriteSheet;
    private SpriteSheet runRightSpriteSheet;
    private SpriteSheet runLeftSpriteSheet;
    private SpriteSheet hitLeftSpriteSheet;
    private SpriteSheet hitRightSpriteSheet;
    private SpriteSheet cloudGroundSpriteSheet;
    private SpriteSheet cloudOffGroundSpriteSheet;

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
        initializeSpriteSheets();
        this.hp = enemyType.getHp();
        this.speed = enemyType.getSpeed();
        this.damage = enemyType.getDamage();
        this.points = enemyType.getPoints();
        System.out.println(hp);
    }

    public void setHp(int hp) {
        this.hp -= hp;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean getAlive() {
        return isAlive;
    }

    public int getStartPosition() {
        return 0;
    }

    public boolean getLastSpriteRight() {
        return false;
    }

    public void setEnemyhit(boolean enemyhit){
        this.enemyhit = enemyhit;
    }

    public boolean getEnemyHit(){
        return enemyhit;
    }

    public void tick(GraphicsContext gc) {
        //setVelocityY(1);
        handleVelocityX();
        handleVelocityY();
        render(gc);
    }

    protected void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    protected void handleVelocityY() {
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

        if(!isAlive && getVelocityY() == 0){
            setCurrentSpriteSheet(cloudGroundSpriteSheet);
            setCurrentSpriteState(ENEMY_DEAD);
        }else if(!isAlive && getVelocityY() < 0){
            setCurrentSpriteSheet(cloudOffGroundSpriteSheet);
            setCurrentSpriteState(ENEMY_DEAD);
        }else if (getVelocityX() == 0 && getVelocityY() == 0 && (!leftCollision && !rightCollision)) {
            if (lastSpriteRight) {
                lastSpriteRight = true;
                setCurrentSpriteState(ENEMY_IDLING_RIGHT);
                setCurrentSpriteSheet(idleRightSpriteSheet);
            } else {
                lastSpriteRight = false;
                setCurrentSpriteState(ENEMY_IDLING_LEFT);
                setCurrentSpriteSheet(idleLeftSpriteSheet);
            }
        } else if(enemyhit){
            if(getVelocityX() > 0 || getVelocityX() == 0 && lastSpriteRight) {
                setCurrentSpriteState(ENEMY_HIT_RIGHT);
                setCurrentSpriteSheet(hitRightSpriteSheet);
            }else {
                setCurrentSpriteState(ENEMY_HIT_LEFT);
                setCurrentSpriteSheet(hitLeftSpriteSheet);
            }
        }else if (getVelocityY() < 0 && (getVelocityX() > 0 || getVelocityX() == 0 && lastSpriteRight)) {
            lastSpriteRight = true;
            setCurrentSpriteState(ENEMY_JUMPING_RIGHT);
            setCurrentSpriteSheet(runRightSpriteSheet);
        } else if (getVelocityY() < 0 && getVelocityX() <= 0) {
            lastSpriteRight = false;
            setCurrentSpriteState(ENEMY_JUMPING_LEFT);
            setCurrentSpriteSheet(runLeftSpriteSheet);
        } else if (getVelocityY() > 0 && (getVelocityX() > 0 || getVelocityX() == 0 && lastSpriteRight)) {
            lastSpriteRight = true;
            setCurrentSpriteState(ENEMY_FALLING_RIGHT);
            setCurrentSpriteSheet(runRightSpriteSheet);
        } else if (getVelocityY() > 0 && getVelocityX() <= 0) {
            lastSpriteRight = false;
            setCurrentSpriteState(ENEMY_FALLING_LEFT);
            setCurrentSpriteSheet(runLeftSpriteSheet);
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
        idleRightSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_idleRight.png", 4, enemyType.getIdleW(), enemyType.getIdleH());
        idleLeftSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_idleLeft.png" , 4, enemyType.getIdleW(), enemyType.getIdleH());
        runRightSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_runRight.png", 4, enemyType.getRundW(), enemyType.getRunH());
        runLeftSpriteSheet = new SpriteSheet("/Resources/enemies/"  + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_runLeft.png", 4, enemyType.getRundW(), enemyType.getRunH());
        hitRightSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_hitRight.png", 2, enemyType.getHitW(), enemyType.getHitH());
        hitLeftSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_hitLeft.png", 2, enemyType.getHitW(), enemyType.getHitH());
        cloudGroundSpriteSheet = new SpriteSheet("/Resources/cloud/groundCloud.png",5, 132, 88);
        cloudOffGroundSpriteSheet = new SpriteSheet("/Resources/cloud/offGroundCloud.png",5,99,88 );
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
        return new Rectangle(getX() + WIDTH, getY() + 10, 20, 40);
    }

    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle(getX(), getY() + 10, 20,  40);
    }


}
