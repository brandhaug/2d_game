package Game.Levels;

import Game.GameController;
import Game.GameObjects.Coin;
import Game.GameObjects.Enemy;
import Game.GameObjects.Tile;
import Game.GameObjects.TileType;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Level {
    private List<Tile> tiles;
    private List<Coin> coins;
    private List<Enemy> enemies;

    private char[][] map;

    private int coinCounter = 0;


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

    public void parseMap(char[][] map) {
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


    // TODO: Lag egne Exceptions
    public char[][] loadMap(String fileName) throws FileNotFoundException {
        File file = new File(getClass().getResource("/Resources/maps/" + fileName).getPath());
        Scanner scanner = new Scanner(file);

        int index = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (index == 0) {
                String[] headerStrings = line.split(" ");
                int mapHeight = Integer.parseInt(headerStrings[0]);
                int mapWidth = Integer.parseInt(headerStrings[1]);
                map = new char[mapHeight][mapWidth];
            } else {
                line = line.replaceAll("\\s", "");
                char[] lineArr = line.toCharArray();
                map[index - 1] = lineArr;
            }

            index++;
        }

        return map;
    }

    public void tick(GraphicsContext gc, int playerVelocityX, int playerVelocityY, double time) {
        render(gc, playerVelocityX, playerVelocityY, time);
    }

    private void render(GraphicsContext gc, int playerVelocityX, int playerVelocityY, double time) {
        renderStartingPoint(gc, playerVelocityX, playerVelocityY);
        renderTiles(gc, playerVelocityX, playerVelocityY);
        renderCoins(gc, playerVelocityX, playerVelocityY, time);
        renderEnemies(gc, playerVelocityX, playerVelocityY, time);
    }

    private void renderStartingPoint(GraphicsContext gc, int playerVelocityX, int playerVelocityY) {
//        gc.drawImage(startingPointImage, 200 - playerVelocityX, 400);
    }

    private void renderTiles(GraphicsContext gc, int playerVelocityX, int playerVelocityY) {
        for (Tile tile : getTiles()) {
            tile.setX(tile.getX() - playerVelocityX);
//            tile.setY(tile.getY() - playerVelocityY);
            tile.tick(gc);
        }
    }

    private void renderCoins(GraphicsContext gc, int playerVelocityX, int playerVelocityY, double time) {
        for (Coin coin : getCoins()) {
            coin.setX(coin.getX() - playerVelocityX);
//            coin.setY(coin.getY() - playerVelocityY);
            coin.tick(gc);
        }
    }

    private void renderEnemies(GraphicsContext gc, int playerVelocityX, int playerVelocityY, double time) {
        for (Enemy enemy : getEnemies()) {
            enemy.setX(enemy.getX() - playerVelocityX);
            enemy.setY((int) (enemy.getY() - ((int) 300 + 128 * Math.sin(time))));
            enemy.tick(gc);
        }
    }


}
