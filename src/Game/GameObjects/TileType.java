package Game.GameObjects;

public enum TileType {
    GRASS("Grass", 1),
    GRASS_LEFT("GrassLeft", 1),
    GRASS_MID("GrassMid", 1),
    GRASS_RIGHT("GrassRight", 1),
    GRASS_TOP("GrassTop", 1),
    DIRT("Dirt", 1),
    DIRT_BOTTOM("DirtDown", 1),
    DIRT_LEFT("DirtLeft", 1),
    DIRT_RIGHT("DirtRight", 1),
    DIRT_RIGHT_CORNER("DirtRightCorner", 1),
    DIRT_LEFT_CORNER("DirtLeftCorner", 1),
    DIRT_COLUMN("DirtColumn", 1);

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
