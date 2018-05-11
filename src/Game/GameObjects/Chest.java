package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;

public class Chest extends GameObject {
    private SpriteSheet spriteSheet;
    private boolean isAnimated;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Chest(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
        setCurrentSpriteSheet(spriteSheet);
    }

    /**
     * Initializes spritesheet
     */
    @Override
    public void initializeSpriteSheets() {
        spriteSheet = new SpriteSheet("/Resources/chest/chest.png", 1, 65, 65);
    }

    /**
     * Animate chest
     */
    public void animateChest() {
        isAnimated = true;
    }

    public boolean isAnimated() {
        return isAnimated;
    }
}
