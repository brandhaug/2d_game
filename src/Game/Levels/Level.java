package Game.Levels;

import CreateLevel.MapParser;
import Game.GameController;
import Game.GameObjects.*;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Level {
    private List<Tile> tiles;
    private List<Coin> coins;
    private List<Enemy> enemies;
    private List<Enemy> disposeEnemies;
    private Chest chest;

    private int coinCounter;
    private int currentTileY;
    private int lowestTileY;
    private int cameraVelocityY;
    private int playerStartPositionX;
    private int playerStartPositionY;
    private boolean cameraInitialized;

    //Survival
    //TODO: Splitte opp Level og Survival i hver sin klasse?
    int spawnX;
    int spawnY;
    int random;
    int waveNr = 0;
    int killsRequired = 0;
    boolean validSpawn = false;
    private int enemyAmount = 10;
    private boolean survival = false;
    private int [][]spawnSpots = new int [2][67];
    private List<Bullet> bullets;
    private List<Bullet> disposeBullets;
    private List<Ammunition> ammunition;
    private int killCounter = 0;
    private int bulletCounter;

    public Level(String fileName) {
        tiles = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        disposeBullets = new ArrayList<>();
        disposeEnemies = new ArrayList<>();
        ammunition = new ArrayList<>();
        char[][] map = loadMap(fileName);
        parseMap(map);
        bulletCounter = 10;
        if(survival){
            wave();
        }
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

    public List<Enemy> getDisposeEnemies() {
        return disposeEnemies;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Bullet> getDisposeBullets() {
        return disposeBullets;
    }

    public List<Ammunition> getAmmunition() {
        return ammunition;
    }


    public void addCoinCounter() {
        this.coinCounter++;
    }

    public int getCoinCounter() {
        return coinCounter;
    }

    public void addKillCounter() {
        this.killCounter++;
    }

    public int getKillCounter(){
        return killCounter;
    }

    public void decreaseEnemyCounter(){
        this.enemyAmount--;
    }

    public int getBulletCounter() {
        return bulletCounter;
    }

    public void addBulletCounter(int bullets){
        bulletCounter += bullets;
    }

    public void decreaseBulletCounter(){
        bulletCounter -= 1;
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

    public void setSurvival(boolean survival){
        this.survival = survival;
    }

    public boolean getSurvival(){
        return survival;
    }

    public void tick(GraphicsContext gc, Player player, double time) {
        handleCameraVelocity(player);
        render(gc, player, time);
        if(survival)spawnEnemies(player);
        /*
        System.out.println("Kills required: " + killsRequired);
        System.out.println("Enemies killed: " + killCounter);
        System.out.println("Enemy Amount: " + enemyAmount);
        */
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
        renderAmmunition(gc,player);


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
        if (chest != null) {
            chest.setX(chest.getX() - player.getVelocityX());
            chest.setY(chest.getY() + cameraVelocityY);
            chest.tick(gc);
        }
    }

    private void renderAmmunition(GraphicsContext gc, Player player) {
        for (Ammunition ammunition : getAmmunition()) {
            ammunition.setX(ammunition.getX() - player.getVelocityX());
            ammunition.setY(ammunition.getY() + cameraVelocityY);
            ammunition.tick(gc);
        }
    }

    private void renderEnemies(GraphicsContext gc, Player player) {
        for (Enemy enemy : getEnemies()) {

            //TODO Make better Enemy AI
            if (!enemy.getLeftCollision() && enemy.getX() > GameController.PLAYER_X_MARGIN) {
                enemy.setVelocityX(-enemy.getSpeed(), false);
                enemy.setRightCollision(false);
            }else if (!enemy.getRightCollision() && enemy.getX() < GameController.PLAYER_X_MARGIN) {
                enemy.setVelocityX(enemy.getSpeed(), false);
                enemy.setLeftCollision(false);
            } else {
                enemy.setVelocityX(0, false);
            }

            enemy.setX(enemy.getX() - player.getVelocityX());
            enemy.handleSpriteState();
            enemy.setY(enemy.getY() + cameraVelocityY);

            //TODO fix spawn-fail (first enemy spawning)
            if(enemy.getY() < 0 || enemy.getY() > 5000 || enemy.getX() < -5000 || enemy.getX() > 5000){
                System.out.println("SPAWN-FAIL: Enemy DELETED!");
                enemyAmount++;
                disposeEnemies.add(enemy);
            }
            enemy.tick(gc);
        }
        enemies.removeAll(disposeEnemies);
    }

    private void renderEnemies(GraphicsContext gc, Player player, double time) {
        for (Enemy enemy : getEnemies()) {
            enemy.setX(enemy.getX() - player.getVelocityX());
            enemy.setY((int) (300 + (128 * Math.sin(time))));
            enemy.tick(gc);
        }
    }

    public int getWaveNr(){
        return waveNr;
    }

    private void wave(Player player){
        waveNr++;
        enemyAmount = 10*waveNr;
        killsRequired += enemyAmount;
        for (int i = 0; i < waveNr; i++) {
            ammunition.add(new Ammunition(spawnX - player.getX() + GameController.PLAYER_X_MARGIN,spawnY));
        }
    }

    private void wave(){
        waveNr++;
        enemyAmount = 10*waveNr;
        killsRequired += enemyAmount;
    }

    private void spawnEnemies(Player player) {
        while(!validSpawn) {
            random = ThreadLocalRandom.current().nextInt(0, spawnSpots[0].length);
            spawnX = spawnSpots[0][random];
            spawnY = spawnSpots[1][random];
            if (spawnX <= player.getX() - GameController.PLAYER_X_MARGIN || spawnX >= player.getX() + GameController.PLAYER_X_MARGIN) validSpawn = true;
        }
        validSpawn = false;

            if (enemyAmount == 0) {
                if (killCounter >= killsRequired) {
                    wave(player);
                } else {
                    return;
                }
            } else if (enemyAmount <= 20 && enemyAmount > 0) {
                if (spawnY == 89) {
                    enemies.add(new Enemy(spawnX - player.getX() + GameController.PLAYER_X_MARGIN, spawnY, EnemyType.ENEMY_A));
                } else {
                    enemies.add(new Enemy(spawnX - player.getX() + GameController.PLAYER_X_MARGIN, spawnY * 4, EnemyType.ENEMY_A));
                }
                enemyAmount--;
            } else if (enemyAmount <= 40 && enemyAmount > 20) {
                if (spawnY == 88) {
                    enemies.add(new Enemy(spawnX - player.getX() + GameController.PLAYER_X_MARGIN, spawnY, EnemyType.ENEMY_C));
                } else {
                    enemies.add(new Enemy(spawnX - player.getX() + GameController.PLAYER_X_MARGIN, spawnY * 4, EnemyType.ENEMY_C));
                }
                enemyAmount--;
            } else if (enemyAmount <= 100 && enemyAmount > 40) {
                if (spawnY == 88) {
                    enemies.add(new Enemy(spawnX - player.getX() + GameController.PLAYER_X_MARGIN, spawnY, EnemyType.ENEMY_C));
                } else {
                    enemies.add(new Enemy(spawnX - player.getX() + GameController.PLAYER_X_MARGIN, spawnY * 4, EnemyType.ENEMY_C));
                }
                enemyAmount--;
            }
    }


    public void renderBullets(GraphicsContext gc,Player player) {
        Iterator<Bullet> iterator = getBullets().iterator();

        while(iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.setY(bullet.getY() + cameraVelocityY);
            if(bullet.getFacing() > 0){
                bullet.setVelocityX(50, true);
                bullet.setX(bullet.getX() - player.getVelocityX());
                if (player.getVelocityX() >= 0) {
                    bullet.setX(bullet.getX() + player.getVelocityX());
                }

            }else{
                    bullet.setVelocityX(-50, true);
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


    private void parseMap(char[][] map) {
        final char TILE = '-';
        final char AMMO = '.';
        final char COIN = 'C';
        final char SPAWN = 'x';
        final char ENEMYA = 'a';
        final char ENEMYC = 'c';
        final char START = 's';
        final char END = 'f';

        final int COIN_SIZE = 64;
        int spawnIndex = 0;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                switch (map[y][x]) {
                    case (TILE):
                        if (y > 0 && map[y - 1][x] == TILE) {
                            //Dirt
                            if ((y < map.length - 1 && map[y + 1][x] != TILE) || y == map.length - 1) {
                                // Bottom dirt
                                if (((x > 0 && map[y][x - 1] == TILE)) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                                    // Bottom dirt with tiles on both sides
                                    tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.DIRT_BOTTOM));
                                } else if ((x > 0 && map[y][x - 1] == TILE || x == 0) && (x < map[y].length - 1 && map[y][x + 1] != TILE)) {
                                    // Bottom dirt with tile on left
                                    tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.DIRT_RIGHT_CORNER));
                                } else if ((x > 0 && map[y][x - 1] != TILE || x == 0) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                                    // Bottom dirt with tile on right
                                    tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.DIRT_LEFT_CORNER));
                                }
                            } else if ((x > 0 && map[y][x - 1] != TILE || x == 0) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                                // Not bottom dirt with tile on right
                                tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.DIRT_LEFT));
                            } else if ((x > 0 && map[y][x - 1] == TILE) && (x < map[y].length - 1 && map[y][x + 1] != TILE)) {
                                tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.DIRT_RIGHT));
                            } else if ((x > 0 && map[y][x - 1] != TILE) && (x < map[y].length - 1 && map[y][x + 1] != TILE)) {
                                tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.DIRT_COLUMN));
                            } else {
                                tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.DIRT));
                            }
                        } else {
                            //Grass
                            if ((x > 0 && map[y][x - 1] != TILE || x == 0) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                                // Grass tile on right
                                tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.GRASS_LEFT));
                            } else if ((x > 0 && map[y][x - 1] == TILE) && (x < map[y].length - 1 && map[y][x + 1] != TILE)) {
                                tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.GRASS_RIGHT));
                            } else if ((x > 0 && map[y][x - 1] == TILE) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                                tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.GRASS_MID));
                            } else {
                                tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, TileType.GRASS_TOP));
                            }
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
                    case (SPAWN):
                        spawnSpots[0][spawnIndex] = x * GameController.TILE_SIZE;
                        spawnSpots[1][spawnIndex] = y;
                        System.out.print("spawnX: "+spawnSpots[0][spawnIndex] + " ");
                        System.out.println("spawnY: "+spawnSpots[1][spawnIndex]);
                        spawnIndex++;
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
