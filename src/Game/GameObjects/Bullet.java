package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Bullet extends GameObject {

    private int damage;
    private boolean facingRight;
    private int height;
    private int width;
    private SpriteSheet spriteSheet;
    BulletType bulletType;

    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param facingRight
     * @param bulletType
     */
    public Bullet(int x, int y, boolean facingRight, BulletType bulletType) {
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

    public boolean getFacingRight() {
        return facingRight;
    }

    public int getDamage() {
        return damage;
    }

    public void initializeSpriteSheets() {
        if (facingRight) {
            spriteSheet = new SpriteSheet("/Resources/bullets/bullet_" + bulletType.getFileName() + "_Right.png", 1, bulletType.getWidth(), bulletType.getHeight());
        } else {
            spriteSheet = new SpriteSheet("/Resources/bullets/bullet_" + bulletType.getFileName() + "_Left.png", 1, bulletType.getWidth(), bulletType.getHeight());
        }
        setCurrentSpriteSheet(spriteSheet);
    }

    private void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    private void handleVelocityY() {
        setY(getY() + getVelocityY());
    }

}
