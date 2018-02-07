package Game.GameObjects;

public class Saw implements GameObject {
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

    }

    @Override
    public int getVelocityX() {
        return 0;
    }

    @Override
    public void setVelocityY(int velocityY) {

    }

    @Override
    public int getVelocityY() {
        return 0;
    }
}
