package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

public class Tile extends GameObject {

    private TileType tileType;
    private final int SIZE = 72;

    /**
     * Sets the x and y position of the tile. Sets the tile type.
     *
     * @param x        the x position
     * @param y        the y position
     * @param tileType the tile type
     */
    public Tile(int x, int y, TileType tileType) {
        super(x, y);
        this.tileType = tileType;
        initializeSpriteSheets();
    }

    /**
     * Initializes the sprite sheets of the tile
     */
    @Override
    public void initializeSpriteSheets() {
        SpriteSheet spriteSheet = new SpriteSheet("/Resources/tiles/" + tileType.getFileName() + ".png", tileType.getCols(), SIZE, SIZE);
        setCurrentSpriteSheet(spriteSheet);
    }

    /**
     * Gets the tile type
     *
     * @return tileType
     */
    public TileType getTileType() {
        return this.tileType;
    }

    /**
     * gets the size of the tile
     *
     * @return size
     */
    public int getSize(){
        return SIZE;
    }
}
