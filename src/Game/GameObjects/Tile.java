package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

public class Tile extends GameObject {

    private TileType tileType;
    private SpriteSheet spriteSheet;
    private final int SIZE = 128;

    public Tile(int x, int y, TileType tileType) {
        super(x, y);
        this.tileType = tileType;
        initializeSpriteSheets();
    }

    @Override
    public void initializeSpriteSheets() {
        spriteSheet = new SpriteSheet("/Resources/tiles/" + tileType.getFileName() + ".png", tileType.getCols(), SIZE, SIZE);
        setCurrentSpriteSheet(spriteSheet);
    }
}
