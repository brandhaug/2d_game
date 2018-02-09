package Game.GameObjects;

import Game.GameController;
import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;

public class Tile extends GameObject {

    private TileType tileType;
    private int x;
    private int y;

    private int velocityX = 0;
    private int velocityY = 0;

    private GraphicsContext gc;

    private SpriteSheet spriteSheet;
    private SpriteSheet currentSpriteSheet;

    private final int SIZE = 128;

    public Tile(GraphicsContext gc, TileType tileType, int x, int y) {
        this.gc = gc;
        this.tileType = tileType;
        this.x = x;
        this.y = y;
        initializeSpriteSheets();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    @Override
    public int getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    private void initializeSpriteSheets() {
        spriteSheet = new SpriteSheet("/Resources/tiles/" + tileType.getFileName() + ".png", tileType.getCols(), SIZE, SIZE);
        currentSpriteSheet = spriteSheet;
    }

    public void tick(int playerX) {
        spriteSheet.draw(gc, x - playerX, y, 0, 1);
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle(x + 10, y + currentSpriteSheet.getSpriteHeight() / 2, currentSpriteSheet.getSpriteWidth() - 20, currentSpriteSheet.getSpriteHeight() / 2);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(x + 10, y, currentSpriteSheet.getSpriteWidth() - 20, currentSpriteSheet.getSpriteHeight() / 2);
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(x + currentSpriteSheet.getSpriteWidth() - 5, y + 5, 5, currentSpriteSheet.getSpriteHeight() - 10);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(x, y + 5, 5, currentSpriteSheet.getSpriteHeight() - 10);
    }

}
