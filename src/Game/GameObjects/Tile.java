package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

public class Tile extends GameObject {

    private TileType tileType;
    private final int SIZE = 72;

    public Tile(int x, int y, TileType tileType) {
        super(x, y);
        this.tileType = tileType;
        initializeSpriteSheets();
    }

    @Override
    public void initializeSpriteSheets() {
        SpriteSheet spriteSheet = new SpriteSheet("/Resources/tiles/" + tileType.getFileName() + ".png", tileType.getCols(), SIZE, SIZE);
        setCurrentSpriteSheet(spriteSheet);
    }

    public int getSize(){
        return SIZE;
    }
}
