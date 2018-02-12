package Game.Levels;

import Game.GameController;
import Game.GameObjects.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Beginner extends Level {

    private Image startingPointImage;
    protected int coinCounter = 0;

    public Beginner() {
        this.startingPointImage = new Image("/Resources/start.png");
        addTiles();
        addCoins();
        addEnemies();
    }

    private void addTiles() {
        for (int x = 0; x <= 90 * 64; x += GameController.TILE_SIZE) {
            if (x == 0) {
                for (int y = 0; y < 12 * GameController.TILE_SIZE; y += GameController.TILE_SIZE) {
                    getTiles().add(new Tile(x, y, TileType.GRASS_MID));
                }
            } else {
                getTiles().add(new Tile(x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE, TileType.GRASS_MID));
                getTiles().add(new Tile(x, 200, TileType.GRASS_LEFT));
            }
        }

        getTiles().add(new Tile(500, 500, TileType.GRASS_MID));
    }

    public void addCoinCounter(){
        this.coinCounter++;
    }

    public int getCoinCounter(){
        return coinCounter;
    }

    private void addCoins() {
        getCoins().add(new Coin(600, 400));
        getCoins().add(new Coin(300, 400));
    }

    private void addEnemies() {
        getEnemies().add(new Enemy(800, 500));
    }

    public void tick(GraphicsContext gc, int playerVelocityX, double time) {
        render(gc, playerVelocityX, time);
    }

    private void render(GraphicsContext gc, int playerVelocityX, double time) {
        renderStartingPoint(gc, playerVelocityX);
        renderTiles(gc, playerVelocityX);
        renderCoins(gc, playerVelocityX, time);
        renderEnemies(gc, playerVelocityX, time);
    }

    private void renderStartingPoint(GraphicsContext gc, int playerVelocityX) {
//        gc.drawImage(startingPointImage, 200 - playerVelocityX, 400);
    }

    private void renderTiles(GraphicsContext gc, int playerVelocityX) {
        for (Tile tile : getTiles()) {
            tile.setX(tile.getX() - playerVelocityX);
            tile.tick(gc);
        }
    }

    private void renderCoins(GraphicsContext gc, int playerVelocityX, double time) {
        for (Coin coin : getCoins()) {
            coin.setX(coin.getX() - playerVelocityX);
            coin.tick(gc);
        }
    }

    private void renderEnemies(GraphicsContext gc, int playerVelocityX, double time) {
        for (Enemy enemy : getEnemies()) {
            enemy.setX(enemy.getX() - playerVelocityX);
            enemy.setY((int) (300 + 128 * Math.sin(time)));
            enemy.tick(gc);
        }
    }
}
