package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

public class Enemy extends GameObject {
    private SpriteSheet spriteSheet;

    public Enemy(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
    }

    public void initializeSpriteSheets() {
        spriteSheet = new SpriteSheet("/Resources/enemies/Mace.png", 1, 128, 128);
        setCurrentSpriteSheet(spriteSheet);
    }
}
