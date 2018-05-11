package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

public class Coin extends GameObject {

    /**
     * Setting x and y for Coin. Initializing sprite sheets
     *
     * @param x the x position
     * @param y the y position
     */
    public Coin(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
    }

    /**
     * Initializing the sprite sheets for the coin animation
     */
    public void initializeSpriteSheets() {
        SpriteSheet spriteSheet = new SpriteSheet("/Resources/coin/CoinAnimation3.png", 16, 64, 64);
        setCurrentSpriteSheet(spriteSheet);
    }
}
