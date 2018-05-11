package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

public class Bullet extends GameObject {

    private short damage;
    private boolean facingRight;
    private short width;
    private short height;
    private BulletType bulletType;

    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param facingRight
     * @param bulletType
     */
    public Bullet(short x, short y, boolean facingRight, BulletType bulletType) {
        super(x, y);
        this.facingRight = facingRight;
        this.bulletType = bulletType;
        this.damage = bulletType.getDamage();
        this.width = bulletType.getWidth();
        this.height = bulletType.getHeight();
        initializeSpriteSheets();
    }

    /**
     * Handles velocity x and y
     * Renders bullet
     *
     * @param gc
     */
    public void tick(GraphicsContext gc) {
        handleVelocityX();
        handleVelocityY();
        render(gc);
    }

    /**
     * Get facing right
     * @return facingRight
     */
    public boolean getFacingRight() {
        return facingRight;
    }

    /**
     * Get damage
     * @return damage
     */
    public short getDamage() {
        return damage;
    }

    /**
     * Initialize spritesheets depending on facingRight
     */
    public void initializeSpriteSheets() {
        SpriteSheet spriteSheet;
        
        if (facingRight) {
            spriteSheet = new SpriteSheet("/Resources/bullets/bullet_" + bulletType.getFileName() + "_Right.png", 1, bulletType.getWidth(), bulletType.getHeight());
        } else {
            spriteSheet = new SpriteSheet("/Resources/bullets/bullet_" + bulletType.getFileName() + "_Left.png", 1, bulletType.getWidth(), bulletType.getHeight());
        }
        setCurrentSpriteSheet(spriteSheet);
    }

    /**
     * Add velocityX to X
     */
    private void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    /**
     * Add velocityY to Y
     */
    private void handleVelocityY() {
        setY(getY() + getVelocityY());
    }

}
