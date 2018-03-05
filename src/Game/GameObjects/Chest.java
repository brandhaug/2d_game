package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

/**
 * Created by stgr99 on 05.03.2018.
 */
public class Chest extends GameObject {

    private SpriteSheet spriteSheet;
    private boolean isAnimated;

    public Chest(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
        setCurrentSpriteSheet(spriteSheet);
    }

    @Override
    public void initializeSpriteSheets() {
        spriteSheet = new SpriteSheet("/Resources/chest/chest.png", 1, 65, 65);
    }

    public void animateChest() {
        isAnimated = true;
    }

    public boolean isAnimated() {
        return isAnimated;
    }
}
