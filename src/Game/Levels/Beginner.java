package Game.Levels;

import Game.GameController;
import Game.GameObjects.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Beginner {

    public static final int GROUND_FLOOR_HEIGHT = 128;
    private GraphicsContext gc;
    private long startNanoTime;
    private long currentNanoTime;
    private static List<Tile> tiles;
    private final int tileCount = 30;
    private int playerX;

    public Beginner(GraphicsContext gc) {
        this.gc = gc;
        this.tiles = new ArrayList<>();
        addTiles();
    }

    public void draw() {
        drawEnemies();
        drawObstacles();
        drawCoin();
    }

    private void drawCoin() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;
        Coin coin1 = new Coin(gc);
        coin1.setY((int) (300 + 128 * Math.sin(t)));
    }

    private void drawEnemies() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;

        Enemy mace1 = new Enemy();
        mace1.setX((int) (1100 + (128 * Math.cos(t)) - playerX));
        mace1.setY((int) (300 + 128 * Math.sin(t)));
        gc.drawImage(new Image("Resources/enemies/Mace.png"), mace1.getX(), mace1.getY());

        Enemy mace2 = new Enemy();
        mace2.setX((int) (1500 + 128 * Math.sin(t)) - playerX);
        mace2.setY((int) (300 + 128 * Math.cos(t)));
        gc.drawImage(new Image("Resources/enemies/Mace.png"), mace2.getX(), mace2.getY());
    }

    private void drawObstacles() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;
        Saw saw1 = new Saw();
        saw1.setY((int) (300 + 128 * Math.sin(t)));
        gc.drawImage(new Image("Resources/obstacles/Saw.png"), saw1.getX(), saw1.getY());
    }

    private void addTiles() {
        for (int x = 0; x <= 1280 - GameController.TILE_SIZE; x += GameController.TILE_SIZE) {
            if (x == 0) {
                for (int y = 0; y < 12 * GameController.TILE_SIZE; y += GameController.TILE_SIZE) {
                    tiles.add(new Tile(gc, TileType.GRASS_LEFT, x, y));
                }
            } else if (x == 1280 - GameController.TILE_SIZE) {
                tiles.add(new Tile(gc, TileType.GRASS_LEFT, x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE));
            } else {
                tiles.add(new Tile(gc, TileType.GRASS_LEFT, x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE - 64));
                tiles.add(new Tile(gc, TileType.GRASS_MID, x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE));
            }
        }
    }

    public void tick(long startNanoTime, long currentNanoTime, int playerX) {
        this.playerX = playerX;
        this.startNanoTime = startNanoTime;
        this.currentNanoTime = currentNanoTime;
        draw();

        for (Tile tile : tiles) {
            tile.tick();
        }
    }

    public static List<Tile> getTiles() {
        return tiles;
    }
}
