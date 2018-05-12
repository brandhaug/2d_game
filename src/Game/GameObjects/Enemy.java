package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Enemy extends GameObject{

    private int hp;
    private int speed;
    private int damage;
    private int points;
    private int width;
    private int height;
    private int heightHit;
    private boolean enemyHit = false;

    private boolean rightCollision = false;
    private boolean leftCollision = false;
    private boolean firstCollision = false;
    private boolean lastSpriteRight = true;
    private boolean isAlive = true;
    private EnemyType enemyType;

    private final int WIDTH = 62;

    // States
    private final static int ENEMY_IDLING_RIGHT = 0;
    private final static int ENEMY_RUNNING_RIGHT = 1;
    private final static int ENEMY_JUMPING_RIGHT = 2;
    private final static int ENEMY_FALLING_RIGHT = 3;
    private final static int ENEMY_HIT_RIGHT = 4;
    private final static int ENEMY_IDLING_LEFT = 5;
    private final static int ENEMY_RUNNING_LEFT = 6;
    private final static int ENEMY_JUMPING_LEFT = 7;
    private final static int ENEMY_FALLING_LEFT = 8;
    private final static int ENEMY_HIT_LEFT = 9;

    // Spritesheets
    private SpriteSheet idleRightSpriteSheet;
    private SpriteSheet idleLeftSpriteSheet;
    private SpriteSheet runRightSpriteSheet;
    private SpriteSheet runLeftSpriteSheet;
    private SpriteSheet hitLeftSpriteSheet;
    private SpriteSheet hitRightSpriteSheet;

    /**
     * Sets x and y position for the enemy. Sets hp, speed, damage, points, width, height and heightHit from EnemyType.
     * Initializing sprite sheets for enemy.
     *
     * @param x         the x position
     * @param y         the y position
     * @param enemyType the enemy type of the enemy
     * @see EnemyType
     */
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
    }

    /**
     * Sets the enemy hp
     *
     * @param hp enemy hp
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Gets the hp of the enemy
     *
     * @return hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * Gets the damage of the enemy
     *
     * @return damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets the width of the enemy
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the enemy
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the height hit of the enemy
     * @return heightHit
     */
    public int getHeightHit() {
        return heightHit;
    }

    /**
     * Gets the speed of the enemy
     * @return speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the points of the enemy
     * @return points
     */
    public int getPoints(){
        return points;
    }

    /**
     * Sets enemy to alive or dead
     * @param isAlive true if alive, false if dead
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Sets the first collision of the enemy to true
     */
    public void setFirstCollision(){
        firstCollision = true;
    }

    /**
     * Returns true if enemy has had its first collision
     * @return firstCollision
     */
    public boolean getFirstCollision(){
        return firstCollision;
    }

    /**
     * Sets the enemy hit
     * @param enemyHit true if enemy has been hit, false if enemy has not been hit
     */
    public void setEnemyHit(boolean enemyHit){
        this.enemyHit = enemyHit;
    }

    /**
     * Beeing called on every tick. Handles velocity x and y of enemy. Renders the graphics.
     * @param gc The GraphicsContext
     * @see GraphicsContext
     */
    public void tick(GraphicsContext gc) {
        handleVelocityX();
        handleVelocityY();
        if(getVelocityY()<=0) {
            setVelocityY(10);
        }
        render(gc);
    }

    /**
     * Sets the x position of the enemy according to the velocity x of the enemy
     */
    private void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    /**
     * Sets the y position of the enemy according to the velocity y of the enemy
     * The enemy will never fall get a higher velocity then 13 when falling and never lower then -35 when falling.
     */
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

    /**
     * Sets sprite state and sprite sheet depending on x and y velocity.
     */
    @Override
    public void handleSpriteStates() {
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
        } else if(enemyHit){
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

    /**
     * Initializes all the sprite sheets for enemy.
     */
    public void initializeSpriteSheets() {
        idleRightSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_idleRight.png", 4, enemyType.getIdleW(), enemyType.getIdleH());
        idleLeftSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_idleLeft.png" , 4, enemyType.getIdleW(), enemyType.getIdleH());
        runRightSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_runRight.png", 4, enemyType.getRunW(), enemyType.getRunH());
        runLeftSpriteSheet = new SpriteSheet("/Resources/enemies/"  + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_runLeft.png", 4, enemyType.getRunW(), enemyType.getRunH());
        hitRightSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_hitRight.png", 2, enemyType.getHitW(), enemyType.getHitH());
        hitLeftSpriteSheet = new SpriteSheet("/Resources/enemies/" + enemyType.getFileName()+ "/monster_" + enemyType.getFileName() + "_hitLeft.png", 2, enemyType.getHitW(), enemyType.getHitH());
    }

    /**
     * Sets the right collision of the enemy
     * @param rightCollision the right collision
     */
    public void setRightCollision(boolean rightCollision) {
        this.rightCollision = rightCollision;
    }

    /**
     * Sets the left collision of the enemy
     * @param leftCollision the left collision
     */
    public void setLeftCollision(boolean leftCollision) {
        this.leftCollision = leftCollision;
    }

    /**
     * Gets the right collision of the enemy
     * @return true if enemy has a right collision, false if not
     */
    public boolean getRightCollision(){
        return rightCollision;
    }

    /**
     * Gets the left collision of the enemy
     * @return true if enemy has a left collision, false if not
     */
    public boolean getLeftCollision(){
        return leftCollision;
    }

    /**
     * Renders the current sprite sheet
     * @param gc the GraphicsContext
     * @see GraphicsContext
     */
    @Override
    public void render(GraphicsContext gc) {
        getCurrentSpriteSheet().render(gc, getX(), getY(), getCurrentSpriteState(), getLastSpriteState());
    }

    /**
     * Get bounds bottom for enemy
     * @return bottom Rectangle
     */
    @Override
    public Rectangle getBoundsBottom() {
        return new Rectangle(getX(), getY(), 40, 72);
    }

    /**
     * Get bounds top for enemy
     * @return top Rectangle
     */
    @Override
    public Rectangle getBoundsTop() {
        return new Rectangle(getX()+ 20, getY(), 40, 20);
    }

    /**
     * Get bounds right for enemy
     * @return right Rectangle
     */
    @Override
    public Rectangle getBoundsRight() {
        return new Rectangle(getX()+WIDTH, getY() + 10, 20, 40);
    }

    /**
     * Get bounds left for enemy
     * @return left Rectangle
     */
    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle(getX(), getY() + 10, 20,  40);
    }
}
