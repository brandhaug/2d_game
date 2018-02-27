package Game.Levels;

import CreateLevel.MapParser;
import Game.GameController;
import Game.GameObjects.*;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private List<Tile> tiles;
    private List<Coin> coins;
    private List<Enemy> enemies;

    private int coinCounter;
    private int currentTileY;
    private int lowestTileY;
    private int cameraVelocityY;
    private int playerStartPositionX;
    private int playerStartPositionY;
    private boolean cameraInitialized;

    public Level(String fileName) {
        tiles = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        char[][] map = loadMap(fileName);
        parseMap(map);
    }

    private void initializeCameraVelocityY() {
        cameraVelocityY = GameController.PLAYER_Y_MARGIN - playerStartPositionY;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void addCoinCounter() {
        this.coinCounter++;
    }

    public int getCoinCounter() {
        return coinCounter;
    }

    public int getLowestTileY() {
        return lowestTileY;
    }

    public int getPlayerStartPositionX() {
        return playerStartPositionX;
    }

    public int getPlayerStartPositionY() {
        return playerStartPositionY;
    }

    public void tick(GraphicsContext gc, Player player, double time) {
        handleCameraVelocity(player);
        render(gc, player, time);
    }

    private void handleCameraVelocity(Player player) {
        if (!cameraInitialized) {
            initializeCameraVelocityY();
        } else {
            if (player.isIdling() || player.isRunning()) {
                currentTileY = player.getY();
            }

            if (player.isRunning() || player.isIdling() || (player.isFalling() && player.getY() > currentTileY)) {
                if (GameController.CANVAS_HEIGHT - player.getY() < GameController.PLAYER_Y_MARGIN) {
                    cameraVelocityY = -13;
                } else if (GameController.CANVAS_HEIGHT - player.getY() > GameController.PLAYER_Y_MARGIN + 30) {
                    cameraVelocityY = 10;
                } else {
                    cameraVelocityY = 0;
                }
            }
        }
    }

    private void render(GraphicsContext gc, Player player, double time) {
        player.setY(player.getY() + cameraVelocityY);
        renderTiles(gc, player);
        renderCoins(gc, player);
        renderEnemies(gc, player, time);

        if (!cameraInitialized){
            player.setVelocityX(0, false);
            cameraInitialized = true;
        }
    }

    private void renderTiles(GraphicsContext gc, Player player) {
        for (Tile tile : getTiles()) {
            if (tile.getY() > lowestTileY) {
                lowestTileY = (int) tile.getBoundsTop().getY();
            }
            tile.setX(tile.getX() - player.getVelocityX());
            tile.setY(tile.getY() + cameraVelocityY);
            tile.tick(gc);
        }
    }

    private void renderCoins(GraphicsContext gc, Player player) {
        for (Coin coin : getCoins()) {
            coin.setX(coin.getX() - player.getVelocityX());
            coin.setY(coin.getY() + cameraVelocityY);
            coin.tick(gc);
        }
    }

    private void renderEnemies(GraphicsContext gc, Player player, double time) {
        for (Enemy enemy : getEnemies()) {
            enemy.setX(enemy.getX() - player.getVelocityX());
            enemy.setY((int) (300 + (128 * Math.sin(time))));
            enemy.tick(gc);
        }
    }

    private void parseMap(char[][] map) {
        final char TILE = '-';
        final char COIN = 'c';
        final char ENEMY = 'e';
        final char START = 's';

        final int COIN_SIZE = 64;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                switch (map[y][x]) {
                    case (TILE):
                        tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.GRASS_MID));
                        break;
                    case (COIN):
                        coins.add(new Coin((x * GameController.TILE_SIZE) + COIN_SIZE / 2, (y * GameController.TILE_SIZE) + COIN_SIZE / 2));
                        break;
                    case (ENEMY):
                        enemies.add(new Enemy(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE));
                        break;
                    case (START):
                        playerStartPositionX = x * GameController.TILE_SIZE;
                        playerStartPositionY = y * GameController.TILE_SIZE;
                        break;
                }
            }
        }
    }

    public char[][] loadMap(String fileName) {
        File file = new File(getClass().getResource("/Resources/maps/" + fileName).getPath());
        return MapParser.getArrayFromFile(file);
    }
}
