package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import MainMenu.MainMenuController;

public class Ammunition extends GameObject {

    private SpriteSheet spriteSheet;

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
        spriteSheet = new SpriteSheet("/Resources/bullets/ammo_"+ MainMenuController.selectedBullet+".png", 1, 76, 76);
        setCurrentSpriteSheet(spriteSheet);
    }
}
