package Game.GameObjects;

public enum  EnemyType {
    ENEMY_A("A",1,2,1,1,84,76,84,76,86,92),
    ENEMY_C("C",5,2,1,1,78,76,78,76,79,88);

    private String fileName;
    private int hp;
    private int speed;
    private int damage;
    private int points;
    private int idleW;
    private int idleH;
    private int runH;
    private int runW;
    private int hitH;
    private int hitW;

    EnemyType(String fileName, int hp, int speed, int damage, int points, int idleW, int idleH, int runW, int runH, int hitW, int hitH) {
        this.fileName = fileName;
        this.hp = hp;
        this.speed = speed;
        this.damage = damage;
        this.points = points;
        this.idleW = idleW;
        this.idleH = idleH;
        this.runW = runW;
        this.runH = runH;
        this.hitW = hitW;
        this.hitH = hitH;
    }

    public String getFileName() {
        return fileName;
    }

    public int getIdleW(){
        return idleW;
    }

    public int getIdleH(){
        return idleH;
    }

    public int getRundW(){
        return runW;
    }

    public int getRunH(){
        return runH;
    }

    public int getHitW(){
        return hitW;
    }

    public int getHitH(){
        return hitH;
    }

    public void setHp(int damage) {
        this.hp -= damage;
    }

    public int getHp(){
        return hp;
    }

    public int getSpeed(){
        return speed;
    }

    public int getDamage(){
        return damage;
    }

    public int getPoints(){
        return points;
    }
}
