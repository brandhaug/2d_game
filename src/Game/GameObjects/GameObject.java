package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class GameObject {

    private int x;
    private int y;
    private int velocityX = 0;
    private int velocityY = 0;

    private SpriteSheet currentSpriteSheet;

    private int currentSpriteState = 0;
    private int lastSpriteState = 0;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick(GraphicsContext gc) {
        handleSpriteState();
        render(gc);
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

    public void setVelocityX(int inputVelocityX, boolean force) {
        int MAX_VELOCITY_X;
        if (force) {
            MAX_VELOCITY_X = 10000;
        } else {
            MAX_VELOCITY_X = 12;
        }

        if (inputVelocityX >= MAX_VELOCITY_X) {
            velocityX = MAX_VELOCITY_X;
        } else if (inputVelocityX <= MAX_VELOCITY_X * -1) {
            velocityX = MAX_VELOCITY_X * -1;
        } else {
            velocityX = inputVelocityX;
        }
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public SpriteSheet getCurrentSpriteSheet() {
        return currentSpriteSheet;
    }

    public void setCurrentSpriteSheet(SpriteSheet currentSpriteSheet) {
        this.currentSpriteSheet = currentSpriteSheet;
    }

    public int getCurrentSpriteState() {
        return currentSpriteState;
    }

    public void setCurrentSpriteState(int currentSpriteState) {
        this.currentSpriteState = currentSpriteState;
    }

    public int getLastSpriteState() {
        return lastSpriteState;
    }

    public void setLastSpriteState(int lastSpriteState) {
        this.lastSpriteState = lastSpriteState;
    }

    public abstract void initializeSpriteSheets();

    public Rectangle getBoundsBottom() {
        return new Rectangle(x, y + currentSpriteSheet.getSpriteHeight() - 10, currentSpriteSheet.getSpriteWidth(), 10);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(x, y, currentSpriteSheet.getSpriteWidth(), 10);
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(x + currentSpriteSheet.getSpriteWidth() - 10, y + 10, 10, currentSpriteSheet.getSpriteHeight() - 20);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(x, y + 10, 10, currentSpriteSheet.getSpriteHeight() - 20);
    }

    public void handleSpriteState() {
        setLastSpriteState(currentSpriteState);
    }

    public void render(GraphicsContext gc) {
        currentSpriteSheet.render(gc, x, y, currentSpriteState, lastSpriteState);
        currentSpriteSheet.drawBounds(gc, x, y);
    }
}
