package Game.Levels;

import CreateLevel.MapParser;
import Game.GameController;
import Game.GameObjects.*;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Level {
    private List<Tile> tiles;
    private List<Coin> coins;
    private List<Enemy> enemies;

    private char[][] map;

    private boolean firstIntersectionMade;
    private int coinCounter = 0;

    private int currentTileY = Player.START_POSITION_Y - 200;
    private int lastTileY = currentTileY;
    private int diff = 0;
    private int cameraVelocityY = 0;
    private boolean cameraMovingY = false;


    public Level(String fileName) throws FileNotFoundException {
        tiles = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        char[][] map = loadMap(fileName);
        parseMap(map);
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

    public void tick(GraphicsContext gc, Player player, double time) {
        if ((player.isIdling() || player.isRunning())) {
            currentTileY = player.getY();

            if (Math.abs(currentTileY - lastTileY) > 10) {
                cameraMovingY = true;
                diff += currentTileY - lastTileY;
                System.out.println("Diff: " + diff);

                if (diff > 0) {
                    cameraVelocityY = 4;
                } else if (diff < 0) {
                    cameraVelocityY = -4;
                }
            }

            render(gc, player, time);

            currentTileY = player.getY();
            lastTileY = player.getY();
            diff -= cameraVelocityY;

            if (Math.abs(diff) < 10) {
                diff = 0;
                cameraVelocityY = 0;
            }

        } else if (player.isFalling() && player.getY() > (currentTileY + 10)) {
            cameraMovingY = true;
            cameraVelocityY = 4;
            render(gc, player, time);

        } else {
            cameraMovingY = false;
            render(gc, player, time);
        }
    }

    private void render(GraphicsContext gc, Player player, double time) {
        renderTiles(gc, player);
        renderCoins(gc, player);
        renderPlayer(player);
//        renderEnemies(gc, player, time);
    }

    private void renderPlayer(Player player) {
        if (cameraMovingY) {
            player.setY(player.getY() - cameraVelocityY);
        }
    }

    private void renderTiles(GraphicsContext gc, Player player) {
        for (Tile tile : getTiles()) {
            tile.setX(tile.getX() - player.getVelocityX());
            if (cameraMovingY) {
                tile.setY(tile.getY() - cameraVelocityY);
            }
            tile.tick(gc);
        }
    }

    private void renderCoins(GraphicsContext gc, Player player) {
        for (Coin coin : getCoins()) {
            coin.setX(coin.getX() - player.getVelocityX());
            if (cameraMovingY) {
                coin.setY(coin.getY() - cameraVelocityY);
            }
            coin.tick(gc);
        }
    }

    private void renderEnemies(GraphicsContext gc, Player player, double time) {
        for (Enemy enemy : getEnemies()) {
            enemy.setX(enemy.getX() - player.getVelocityX());
            if (cameraMovingY) {
                enemy.setY((int) (300 + (128 * Math.sin(time))));
            }
            enemy.tick(gc);
        }
    }

    private void parseMap(char[][] map) {
        final char TILE = '-';
        final char COIN = 'c';
        final char ENEMY = 'e';

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
                }
            }
        }
    }

    public char[][] loadMap(String fileName) {
        File file = new File(getClass().getResource("/Resources/maps/" + fileName).getPath());
        return MapParser.getArrayFromFile(file);
    }

    private void renderStartingPoint(GraphicsContext gc, int playerVelocityX, int playerVelocityY) {
//        gc.drawImage(startingPointImage, 200 - playerVelocityX, 400);
    }
}
