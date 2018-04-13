package Game.GameObjects;

public enum  EnemyType {
    //FileName,hp,speed,damage,points,idleW,idleH,runW,runH,hitW,hitH
    ENEMY_A("A",1, 2, 1,  1,84,76,84,76,86, 92),
    ENEMY_B("B",2, 2, 2,  3,78,76,78,76,79, 88),
    ENEMY_C("C",3, 2, 4,  5,68,83,68,83,70,101),
    ENEMY_D("D",4, 2, 5, 10,85,95,85,95,87, 93),
    ENEMY_E("E",5, 3, 6, 20,83,76,83,76,83, 98),
    ENEMY_F("F",6, 3, 7, 30,65,76,65,76,65, 91),
    ENEMY_G("G",7, 3, 8, 50,69,76,69,76,69, 81),
    ENEMY_H("H",8, 3,10, 70,65,76,65,76,65, 90),
    ENEMY_I("I",10,4,15,100,99,77,99,77,99, 89);

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

    public int getRunW(){
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
