package Game.Levels;

import CreateLevel.MapParser;
import Game.GameController;
import Game.GameObjects.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.*;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sqrt;

public class Level {
    private List<Tile> tiles;
    private List<Coin> coins;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private List<Bullet> disposeBullets;
    private Chest chest;

    private int coinCounter;
    private int currentTileY;
    private int lowestTileY;
    private int cameraVelocityY;
    private int playerStartPositionX;
    private int playerStartPositionY;
    private boolean cameraInitialized;
    private int bulletCounter;

    public Level(String fileName) {
        tiles = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        disposeBullets = new ArrayList<>();
        char[][] map = loadMap(fileName);
        parseMap(map);
        int bullets = loadBullets(fileName);
        bulletCounter = bullets;
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

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Bullet>getDisposeBullets(){
        return disposeBullets;
    }

    public void addCoinCounter() {
        this.coinCounter++;
    }

    public int getCoinCounter() {
        return coinCounter;
    }

    public int getBulletCounter() {
        return bulletCounter;
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
        renderEnemies(gc, player);
        renderBullets(gc, player);
        renderChest(gc, player);


        if (!cameraInitialized) {
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

    private void renderChest(GraphicsContext gc, Player player) {
        chest.setX(chest.getX() - player.getVelocityX());
        chest.setY(chest.getY() + cameraVelocityY);
        chest.tick(gc);
    }


    private void renderEnemies(GraphicsContext gc, Player player) {
        for (Enemy enemy : getEnemies()) {

            if(!enemy.getAlive()){
                enemy.setVelocityX(0,false);
            }else {
                if (enemy.getY() < player.getY()) {
                    enemy.setVelocityY(7);
                }
                if (enemy.getX() > GameController.PLAYER_X_MARGIN) {
                    enemy.setVelocityX(-3, true);
                    enemy.setX(enemy.getX() - player.getVelocityX());

                } else if (enemy.getX() < GameController.PLAYER_X_MARGIN) {
                    enemy.setVelocityX(2, true);
                    enemy.setX(enemy.getX() - player.getVelocityX());
                } else {
                    enemy.setVelocityY(-10);
                    enemy.setVelocityX(-1,false);
                }
            }
            enemy.handleSpriteState();
            enemy.setY(enemy.getY() + cameraVelocityY);
            /*
            System.out.println("Enemy VELOCITY: " + enemy.getVelocityX());
            System.out.println("Enemy X: " + enemy.getX());
            System.out.println("Player X: " + player.getX());
            System.out.println("-------------------");
            */
            System.out.println(enemy.getVelocityY());
            enemy.tick(gc);
        }
    }

    private void renderEnemies(GraphicsContext gc, Player player, double time) {
        for (Enemy enemy : getEnemies()) {
            enemy.setX(enemy.getX() - player.getVelocityX());
            enemy.setY((int) (300 + (128 * Math.sin(time))));
            enemy.tick(gc);
        }
    }


    public void renderBullets(GraphicsContext gc,Player player) {
        Iterator<Bullet> iterator = getBullets().iterator();

        while(iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.setY(bullet.getY() + cameraVelocityY);
            if(bullet.getfacing() > 0){
                bullet.setVelocityX(50, true);
                bullet.setX(bullet.getX() - player.getVelocityX());
                if (player.getVelocityX() >= 0) {
                    bullet.setX(bullet.getX() + player.getVelocityX());
                }

            }else{
                    bullet.setVelocityX(-50, true);
                    bullet.setX(bullet.getX() + player.getVelocityX());
                    bullet.setX(bullet.getX() - player.getVelocityX());
                }
            if(bullet.getX() > GameController.CANVAS_WIDTH || bullet.getX() < 0){
                disposeBullets.add(bullet);
            }
            bullet.tick(gc);
            }
        bullets.removeAll(disposeBullets);
    }


    public void addBullet(Bullet b){
        bullets.add(b);
    }

    public void removeBullet(Bullet b){
        bullets.remove(b);
    }

    private void parseMap(char[][] map) {
        final char TILE = '-';
        final char COIN = 'C';
        final char ENEMYA = 'a';
        final char ENEMYC = 'c';
        final char START = 's';
        final char END = 'f';

        final int COIN_SIZE = 64;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                switch (map[y][x]) {
                    case (TILE):
                        if (y > 0 && map[y-1][x] == TILE) {
                            tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.DIRT));
                        } else {
                            tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.GRASS_MID));
                        }
                        break;
                    case (COIN):
                        coins.add(new Coin((x * GameController.TILE_SIZE) + COIN_SIZE / 2, (y * GameController.TILE_SIZE) + COIN_SIZE / 2));
                        break;
                    case (ENEMYA):
                            enemies.add(new Enemy(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, EnemyType.ENEMY_A));
                        break;
                    case (ENEMYC):
                            enemies.add(new Enemy(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, EnemyType.ENEMY_C));
                        break;
                    case (START):
                        playerStartPositionX = x * GameController.TILE_SIZE;
                        playerStartPositionY = y * GameController.TILE_SIZE;
                        break;
                    case (END):
                        chest = new Chest(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE);
                }
            }
        }
    }

    public char[][] loadMap(String fileName) {
        File file = new File(getClass().getResource("/Resources/maps/" + fileName).getPath());
        return MapParser.getArrayFromFile(file);
    }

    public int loadBullets(String fileName) {
        File file = new File(getClass().getResource("/Resources/maps/" + fileName).getPath());
        return MapParser.getBulletAmount(file);
    }

    public Chest getChest() {
        return chest;
    }
}
