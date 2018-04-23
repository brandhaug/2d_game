package Game.GameObjects;

import Game.Levels.Level;
import Game.SpriteSheets.SpriteSheet;
import MainMenu.MainMenuController;

public class Ammunition extends GameObject {

    private SpriteSheet spriteSheet;

    public Ammunition(int x, int y) {
        super(x, y);
        initializeSpriteSheets();
    }

    @Override
    public void initializeSpriteSheets() {
        spriteSheet = new SpriteSheet("/Resources/bullets/ammo_"+ MainMenuController.selectedBullet+".png", 1, 76, 76);
        setCurrentSpriteSheet(spriteSheet);
    }
}
