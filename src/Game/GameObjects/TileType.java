package Game.GameObjects;

public enum TileType {
    GRASS_LEFT("GrassLeft", 1),
    GRASS_MID("GrassMid", 1),
    GRASS_RIGHT("GrassRight", 1);

    private String fileName;
    private int cols;

    TileType(String fileName, int cols) {
        this.fileName = fileName;
        this.cols = cols;
    }

    public String getFileName() {
        return fileName;
    }

    public int getCols() {
        return cols;
    }
}
