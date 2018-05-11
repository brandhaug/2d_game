package Game.GameObjects;

public enum BulletType {

    BULLET_A("A", (short) 1, (short) 35, (short) 2, (short) 30, (short) 14),
    BULLET_B("B", (short) 2, (short) 35, (short) 5, (short) 30, (short) 14),
    BULLET_C("C", (short) 3, (short) 50, (short) 10, (short) 44, (short) 14);

    private String fileName;
    private short damage;
    private short speed;
    private short fireRate;
    private short height;
    private short width;

    /**
     * Constructor
     * @param fileName
     * @param damage
     * @param speed
     * @param fireRate
     * @param width
     * @param height
     */
    BulletType(String fileName, short damage, short speed, short fireRate, short width, short height) {
        this.fileName = fileName;
        this.damage = damage;
        this.speed = speed;
        this.fireRate = fireRate;
        this.height = height;
        this.width = width;
    }

    /**
     * Get filename
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get damage
     * @return damage
     */
    public short getDamage() {
        return damage;
    }

    /**
     * Get speed
     * @return speed
     */
    public short getSpeed() {
        return speed;
    }

    /**
     * Get fire rate
     * @return fireRate
     */
    public double getFireRate() {
        return fireRate;
    }

    /**
     * Get width
     * @return width
     */
    public short getWidth() {
        return width;
    }

    /**
     * Get height
     * @return height
     */
    public short getHeight() {
        return height;
    }
}
