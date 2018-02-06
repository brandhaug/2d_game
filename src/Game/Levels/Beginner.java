package Game.Levels;

import Game.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Beginner {
    public Beginner(GraphicsContext gc) {
        draw(gc);
    }

    public void draw (GraphicsContext gc) {
        Image tile;
        for (int x = 0; x <= 1280 - GameController.TILE_SIZE; x += GameController.TILE_SIZE) {
            if (x == 0) {
                tile = new Image("file:res/images/tiles/128/GrassLeft.png");
            } else if (x == 1280 - GameController.TILE_SIZE) {
                tile = new Image("file:res/images/tiles/128/GrassRight.png");
            } else {
                tile = new Image("file:res/images/tiles/128/GrassMid.png");
            }
            gc.drawImage(tile, x, GameController.HEIGHT - GameController.TILE_SIZE);
        }
    }
}
