package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class Bullet extends GameObject {

    private int damage;
    private int facing;

    private SpriteSheet spriteSheet;

    public Bullet(int x, int y, int damage, int facing) {
        super(x,y);
        this.facing = facing;
        this.damage = damage;
        initializeSpriteSheets();
    }

    public void tick(GraphicsContext gc) {
        handleVelocityX();
        handleVelocityY();
        spriteSheet.drawBounds(gc,getX(),getY());
        render(gc);
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
            if(facing > 0) spriteSheet = new SpriteSheet("/Resources/bullets/bullet.png", 1, 30, 18);
            if(facing < 0) spriteSheet = new SpriteSheet("/Resources/bullets/bulletLeft.png", 1, 30, 18);
            setCurrentSpriteSheet(spriteSheet);
    }

    private void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    private void handleVelocityY() {
        setY(getY() + getVelocityY());
    }

}
