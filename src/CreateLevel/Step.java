package CreateLevel;

public class Step {
    private int x;
    private int y;
    private char currentValue;
    private char lastValue;

    /**
     * Initializes the variables given from the parameters
     * @param x the x position
     * @param y the y position
     * @param currentValue the current value
     * @param lastValue the last value
     */
    Step(int x, int y, char currentValue, char lastValue) {
        this.x = x;
        this.y = y;
        this.currentValue = currentValue;
        this.lastValue = lastValue;
    }

    /**
     * Gets the x position
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x position
     * @param x position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y position
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y position
     * @param y position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the current value
     * @return currentValue
     */
    char getCurrentValue() {
        return currentValue;
    }

    /**
     * Sets the current value
     * @param currentValue
     */
    public void setCurrentValue(char currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * Gets the last value
     * @return lastValue
     */
    char getLastValue() {
        return lastValue;
    }

    /**
     * Sets the last value
     * @param lastValue
     */
    public void setLastValue(char lastValue) {
        this.lastValue = lastValue;
    }
}
