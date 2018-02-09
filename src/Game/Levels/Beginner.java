package Game.Levels;

import Game.GameController;
import Game.GameObjects.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Beginner extends Level {

    private Image startingPointImage;
    
    public Beginner() {
        this.startingPointImage = new Image("/Resources/start.png");
        addTiles();
        addCoins();
        addEnemies();
    }


    public void tick(GraphicsContext gc, int playerX, double time) {
        render(gc, playerX, time);
    }

    private void render(GraphicsContext gc, int playerX, double time) {
        renderStartingPoint(gc, playerX);
        renderTiles(gc, playerX);
        renderCoins(gc, playerX, time);
        renderEnemies(gc, playerX, time);
    }
    
    private void addTiles() {
        for (int x = 0; x <= 90 * 64; x += GameController.TILE_SIZE) {
            if (x == 0) {
                for (int y = 0; y < 12 * GameController.TILE_SIZE; y += GameController.TILE_SIZE) {
                    getTiles().add(new Tile(x, y, TileType.GRASS_LEFT));
                }
            } else if (x == 40 * 64 - GameController.TILE_SIZE) {
                getTiles().add(new Tile(x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE, TileType.GRASS_LEFT));
            } else {
                getTiles().add(new Tile(x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE, TileType.GRASS_MID));
            }
        }

        getTiles().add(new Tile(2000, 500, TileType.GRASS_MID));
    }

    private void addCoins() {
        getCoins().add(new Coin(600, 400));
    }

    private void addEnemies() {
        getEnemies().add(new Enemy(800, 500));
    }
    
    private void renderStartingPoint(GraphicsContext gc, int playerX) {
        gc.drawImage(startingPointImage, 405 - playerX, 400);
    }

    private void renderTiles(GraphicsContext gc, int playerX) {
        for (Tile tile : getTiles()) {
            tile.setX(tile.getX() - playerX);
            tile.render(gc);
        }
    }

    private void renderCoins(GraphicsContext gc, int playerX, double time) {
        for (Coin coin : getCoins()) {
            coin.setX((int) (300 + 128 * Math.sin(time)) - playerX);
            coin.render(gc);
        }
    }

    private void renderEnemies(GraphicsContext gc, int playerX, double time) {
        for (Enemy enemy : getEnemies()) {
            enemy.setX((int) (1100 + (128 * Math.cos(time))) - playerX);
            enemy.setY((int) (300 + 128 * Math.sin(time)));
            enemy.render(gc);
        }
    }
}
