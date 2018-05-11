package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import MainMenu.MainMenuController;

public class Ammunition extends GameObject {

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Ammunition(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
    }

    /**
     * Initializes sprite sheets
     * Sets current spritesheet
     */
    @Override
    public void initializeSpriteSheets() {
        SpriteSheet spriteSheet = new SpriteSheet("/Resources/bullets/ammo_" + MainMenuController.selectedBullet + ".png", 1, 76, 76);
        setCurrentSpriteSheet(spriteSheet);
    }
}
