package Game.GameObjects;

/**
 * Created by stgr99 on 07.02.2018.
 */
public abstract class GameObject {

    private int x;
    private int y;
    private int velocityX = 0;
    private int velocityY = 5;

    void setX(int x) {
        this.x = x;
    }

    int getX() {
        return x;
    }

    void setY(int y) {
        this.y = y;
    }

    int getY() {
        return y;
    }

    void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    int getVelocityX() {
        return velocityX;
    }

    void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    int getVelocityY() {
        return velocityY;
    }
}
