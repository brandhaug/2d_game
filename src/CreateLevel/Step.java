package CreateLevel;

public class Step {
    private int x;
    private int y;
    private char currentValue;
    private char lastValue;

    public Step(int x, int y, char currentValue, char lastValue) {
        this.x = x;
        this.y = y;
        this.currentValue = currentValue;
        this.lastValue = lastValue;
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

    public char getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(char currentValue) {
        this.currentValue = currentValue;
    }

    public char getLastValue() {
        return lastValue;
    }

    public void setLastValue(char lastValue) {
        this.lastValue = lastValue;
    }
}
