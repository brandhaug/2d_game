package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

public class Coin extends GameObject {

    private SpriteSheet spriteSheet;

    public Coin(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
    }

    public void initializeSpriteSheets() {
        spriteSheet = new SpriteSheet("/Resources/coin/CoinAnimation3.png", 16, 64,64);
        setCurrentSpriteSheet(spriteSheet);
    }
}
