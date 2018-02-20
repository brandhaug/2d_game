package Game.Levels;

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

    private boolean moveCameraY;
    private boolean firstIntersectionMade;

    private int coinCounter = 0;
    private int lastTileY;
    private int currentTileY;
    private int divider = 10;


    public Level(String fileName) throws FileNotFoundException {
        tiles = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        firstIntersectionMade = false;
        moveCameraY = false;
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
        setPlayerIntersectionHeights(player);
        render(gc, player, time);

        if (moveCameraY && firstIntersectionMade) {
            //System.out.println("Move Camera: " + (currentTileY - lastTileY));
            System.out.println(player.getY());
        }

        if (!firstIntersectionMade) {
            for (Tile tile : getTiles()) {
                if (player.getBoundsBottom().intersects(tile.getBoundsTop()) && !player.isFalling()) {
                    firstIntersectionMade = true;
                    break;
                }
            }
        }

        if (moveCameraY && firstIntersectionMade) {
            divider -= 1;
            if (divider == 0) {
                moveCameraY = false;
                divider = 10;
            }
        }
    }

    private void setPlayerIntersectionHeights(Player player) {
        if (player.isIdling() || player.isRunning()) {
            int minMovementHeight = 30;
            if (Math.abs(player.getY() - currentTileY) > minMovementHeight) {
                lastTileY = currentTileY;
                moveCameraY = true;
            }
            currentTileY = player.getY();
        }
    }

    private void moveCameraY(Player player, GameObject gameObject) {
        if (moveCameraY && firstIntersectionMade) {
            int cameraDistance = Math.abs(currentTileY - lastTileY);
            //Move camera
            if (currentTileY - lastTileY < 0) {
                if (gameObject != null) {
                    gameObject.setY(gameObject.getY() + (cameraDistance / divider));
                } else {
                    player.setY(player.getY() + (cameraDistance / divider));
                }
            } else if (currentTileY - lastTileY > 0) {
                if (gameObject != null) {
                    gameObject.setY(gameObject.getY() - (cameraDistance / divider));
                } else {
                    player.setY(player.getY() - (cameraDistance / divider));
                }
            }
        }
    }

    private void render(GraphicsContext gc, Player player, double time) {
        //Move _PLAYER_ camera Y
        moveCameraY(player, null);

        renderTiles(gc, player);
        renderCoins(gc, player);
        renderEnemies(gc, player, time);
    }

    private void renderTiles(GraphicsContext gc, Player player) {
        for (Tile tile : getTiles()) {
            tile.setX(tile.getX() - player.getVelocityX());
            moveCameraY(player, tile);
            tile.tick(gc);
        }
    }

    private void renderCoins(GraphicsContext gc, Player player) {
        for (Coin coin : getCoins()) {
            coin.setX(coin.getX() - player.getVelocityX());
            moveCameraY(player, coin);
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
    private char[][] loadMap(String fileName) throws FileNotFoundException {
        File file = new File(getClass().getResource("/Resources/maps/" + fileName).getPath());
        Scanner scanner = new Scanner(file);

        int index = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (index == 0) {
                int mapHeight = getValueFromMapHeader(line, "height");
                int mapWidth = getValueFromMapHeader(line, "width");
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
            //enemy.setY(500);
            enemy.setY((int) (((int) 325 + 128 * Math.sin(time))));
            enemy.tick(gc);
        }
    }

    public int getValueFromMapHeader(String header, String name) {
        String reg = "(?<=" + name + ": )[0-9]+";

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(header);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return -1;
    }
}
