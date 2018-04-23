package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Bullet extends GameObject {

    private int damage;
    private int facing;
    private int height;
    private int width;
    private SpriteSheet spriteSheet;
    BulletType bulletType;

    public Bullet(int x, int y, int facing, BulletType bulletType) {
        super(x,y);
        this.facing = facing;
        this.bulletType = bulletType;
        this.damage = bulletType.getDamage();
        this.width = bulletType.getWidth();
        this.height = bulletType.getHeight();
        initializeSpriteSheets();
    }

    int test = 0;
    public void tick(GraphicsContext gc) {
        handleVelocityX();
        handleVelocityY();
        //spriteSheet.drawBounds(gc,getX(),getY());
        render(gc);
        System.out.println(test++);
    }

    public int getFacing (){
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public int getDamage(){
        return damage;
    }

    public void initializeSpriteSheets() {
            if(facing > 0) spriteSheet = new SpriteSheet("/Resources/bullets/bullet_" + bulletType.getFileName() + "_Right.png", 1, bulletType.getWidth(), bulletType.getHeight());
            if(facing < 0) spriteSheet = new SpriteSheet("/Resources/bullets/bullet_" + bulletType.getFileName() + "_Left.png", 1, bulletType.getWidth(), bulletType.getHeight());
            setCurrentSpriteSheet(spriteSheet);
    }

    private void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    private void handleVelocityY() {
        setY(getY() + getVelocityY());
    }

}
