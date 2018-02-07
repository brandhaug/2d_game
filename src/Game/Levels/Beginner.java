package Game.Levels;

import Game.GameController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Beginner {

    public static final int GROUND_FLOOR_HEIGHT = 128;
    private GraphicsContext gc;
    private long startNanoTime;
    private long currentNanoTime;

    public Beginner(GraphicsContext gc) {
        this.gc = gc;
    }

    public void draw(long startNanoTime, long currentNanoTime) {
        this.startNanoTime = startNanoTime;
        this.currentNanoTime = currentNanoTime;
        drawTiles();
        drawEnemies();
    }

    private void drawTiles() {
        Image tile;
        for (int x = 0; x <= 1280 - GameController.TILE_SIZE; x += GameController.TILE_SIZE) {
            if (x == 0) {
                tile = new Image("Resources/tiles/GrassLeft.png");
            } else if (x == 1280 - GameController.TILE_SIZE) {
                tile = new Image("Resources/tiles/GrassRight.png");
            } else {
                tile = new Image("Resources/tiles/GrassMid.png");
            }
            gc.drawImage(tile, x, GameController.HEIGHT - GameController.TILE_SIZE);
        }
    }

    private void drawEnemies() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;
        double x = 900 + 128 * Math.cos(t);
        double y = 300 + 128 * Math.sin(t);
        gc.drawImage(new Image("Resources/enemies/Mace.png"), x, y);
    }
}
