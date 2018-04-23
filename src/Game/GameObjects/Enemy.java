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
    private int width;
    private int height;
    private int heightHit;
    private boolean enemyhit = false;

    private boolean rightCollision = false;
    private boolean leftCollision = false;
    private boolean firstCollision = false;
    public boolean lastSpriteRight = true;
    private boolean isAlive = true;
    EnemyType enemyType;

    private final int WIDTH = 62;

    // States
    public final static int ENEMY_IDLING_RIGHT = 0;
    public final static int ENEMY_RUNNING_RIGHT = 1;
    public final static int ENEMY_JUMPING_RIGHT = 2;
    public final static int ENEMY_FALLING_RIGHT = 3;
    public final static int ENEMY_HIT_RIGHT = 4;
    public final static int ENEMY_IDLING_LEFT = 5;
    public final static int ENEMY_RUNNING_LEFT = 6;
    public final static int ENEMY_JUMPING_LEFT = 7;
    public final static int ENEMY_FALLING_LEFT = 8;
    public final static int ENEMY_HIT_LEFT = 9;
    public final static int ENEMY_DEAD = 10;

    // Spritesheets
    private SpriteSheet staticSpriteSheet;
    private SpriteSheet idleRightSpriteSheet;
    private SpriteSheet idleLeftSpriteSheet;
    private SpriteSheet runRightSpriteSheet;
    private SpriteSheet runLeftSpriteSheet;
    private SpriteSheet hitLeftSpriteSheet;
    private SpriteSheet hitRightSpriteSheet;
    private SpriteSheet cloudGroundSpriteSheet;
    private SpriteSheet cloudOffGroundSpriteSheet;

    public Enemy(int x, int y, EnemyType enemyType) {
        super(x, y);
        setVelocityY(7);
        this.enemyType = enemyType;
        initializeSpriteSheets();
        this.hp = enemyType.getHp();
        this.speed = enemyType.getSpeed();
        this.damage = enemyType.getDamage();
        this.points = enemyType.getPoints();
        this.width = enemyType.getRunW();
        this.height = enemyType.getRunH();
        this.heightHit = enemyType.getHitH();
        //System.out.println(hp);
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHeightHit() {
        return heightHit;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPoints(){
        return points;
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
        return lastSpriteRight;
    }

    public void setFirstCollision(){
        firstCollision = true;
    }

    public boolean getFirstCollision(){
        return firstCollision;
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
        if(getVelocityY()<=0) {
            setVelocityY(10);
        }
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

        if (getVelocityX() == 0 && getVelocityY() == 0) {
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
        } else if (getVelocityX() > 0) {
            lastSpriteRight = true;
            setCurrentSpriteState(ENEMY_RUNNING_RIGHT);
            setCurrentSpriteSheet(runRightSpriteSheet);
        } else if (getVelocityX() < 0) {
            lastSpriteRight = false;
            setCurrentSpriteState(ENEMY_RUNNING_LEFT);
            setCurrentSpriteSheet(runLeftSpriteSheet);
        }
    }

    public void initializeSpriteSheets() {
        idleRightSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_idleRight.png", 4, enemyType.getIdleW(), enemyType.getIdleH());
        idleLeftSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_idleLeft.png" , 4, enemyType.getIdleW(), enemyType.getIdleH());
        runRightSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_runRight.png", 4, enemyType.getRunW(), enemyType.getRunH());
        runLeftSpriteSheet = new SpriteSheet("/Resources/enemies/"  + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_runLeft.png", 4, enemyType.getRunW(), enemyType.getRunH());
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

    public boolean getRightCollision(){
        return rightCollision;
    }

    public boolean getLeftCollision(){
        return leftCollision;
    }

    @Override
    public void render(GraphicsContext gc) {
        getCurrentSpriteSheet().render(gc, getX(), getY(), getCurrentSpriteState(), getLastSpriteState());

        /*
        // Draw bounds
        gc.setFill(Color.BLACK);
        gc.strokeRect(getX() + 20, getY() + getCurrentSpriteSheet().getSpriteHeight() - 20, WIDTH - 40, 20);
        gc.strokeRect(getX() + 20, getY(), WIDTH - 40, 20);
        gc.strokeRect(getX() + WIDTH - 20, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
        gc.strokeRect(getX(), getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
        */
    }

    @Override
    public Rectangle getBoundsBottom() {
        return new Rectangle(getX(), getY() , 40, 72);
    }

    @Override
    public Rectangle getBoundsTop() {
        return new Rectangle(getX()+ 20, getY(), 40, 20);
    }

    @Override
    public Rectangle getBoundsRight() {
        return new Rectangle(getX()+WIDTH, getY() + 10, 20, 40);
    }

    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle(getX(), getY() + 10, 20,  40);
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
