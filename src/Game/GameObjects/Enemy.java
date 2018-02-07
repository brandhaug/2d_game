package Game.GameObjects;

/**
 * Created by stgr99 on 07.02.2018.
 */
public class Enemy implements GameObject {
    private int x;
    private int y;
    private int velocityX;
    private int velocityY;

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    @Override
    public int getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    @Override
    public int getVelocityY() {
        return velocityY;
    }
}
