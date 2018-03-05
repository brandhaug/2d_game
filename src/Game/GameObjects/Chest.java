package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

/**
 * Created by stgr99 on 05.03.2018.
 */
public class Chest extends GameObject {

    private SpriteSheet spriteSheet;
    private final int SIZE = 65;
    private boolean isAnimated;

    public Chest(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
    }

    @Override
    public void initializeSpriteSheets() {
        spriteSheet = new SpriteSheet("/Resources/chest/chest.png", 1, SIZE, SIZE);
        setCurrentSpriteSheet(spriteSheet);
    }

    public void animateChest() {
        isAnimated = true;
    }

    public boolean isAnimated() {
        return isAnimated;
    }
}
