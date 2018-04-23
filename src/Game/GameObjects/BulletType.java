package Game.GameObjects;

public enum BulletType {
    //FileName,damage,speed,fireRate,width,height
    BULLET_A("A",1, 35, 2, 30,14),
    BULLET_B("B",2, 35, 5, 30,14),
    BULLET_C("C",3, 50, 10 ,44,14);

    private String fileName;
    private int damage;
    private int speed;
    private int fireRate;
    private int height;
    private int width;

    BulletType(String fileName, int damage, int speed, int fireRate, int width, int height) {
        this.fileName = fileName;
        this.damage = damage;
        this.speed = speed;
        this.fireRate = fireRate;
        this.height = height;
        this.width = width;
    }

    public String getFileName() {
        return fileName;
    }
    public int getDamage(){
        return damage;
    }
    public int getSpeed(){
        return speed;
    }
    public double getFireRate(){
        return fireRate;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
}
