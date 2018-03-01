package Game.GameObjects;

public enum  EnemyType {
    PLAYER("player"),
    ENEMYTEST2("idle_left.png"),
    GRASS_RIGHT("GrassRight");

    private String fileName;
    /*
    private int cols;
    private int spriteW;
    private int spriteH;
    */

    EnemyType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
