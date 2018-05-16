package Game.Levels;

import CreateLevel.InvalidMapException;
import CreateLevel.MapParser;
import Game.GameController;
import Game.GameObjects.*;
import Jar.JarUtil;
import MainMenu.MainMenuController;
import javafx.scene.canvas.GraphicsContext;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Level {
    private List<Tile> tiles;
    private List<Coin> coins;
    private List<Enemy> enemies;
    private List<Enemy> disposeEnemies;
    private List<Bullet> bullets;
    private List<Bullet> disposeBullets;
    private List<Ammunition> ammunition;

    private Chest chest;
    private MapParser mapParser;

    private int[][] spawnSpots;

    private int minX = 0;
    private int maxX = 0;
    private int maxY = 0;
    private int coinCounter;
    private int currentTileY;
    private int cameraVelocityY;
    private int playerStartPositionX;
    private int playerStartPositionY;
    private int spawnX;
    private int spawnY;
    private int random;
    private int waveNr = 0;
    private int killsRequired = 0;
    private int killCounter = 0;
    private int bulletCounter;
    private int playerChangeY;
    private int enemyAmount = 0;

    private boolean cameraInitialized;
    private boolean cameraStable;
    private boolean validSpawn = false;
    private boolean enemyAttack;
    private boolean survival = false;

    final char TILE = '-';

    /**
     * Parses the map of specified filename.
     *
     * @param fileName the filename for the selected map
     */
    public Level(String fileName) {
        tiles = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        disposeBullets = new ArrayList<>();
        disposeEnemies = new ArrayList<>();
        ammunition = new ArrayList<>();
        mapParser = new MapParser();
        spawnSpots = new int[2][getNumberOfType('x', fileName)];
        char[][] map = loadMap(fileName);
        parseMap(map);
        playerChangeY = getPlayerStartPositionY();
        bulletCounter = 10;
    }

    /**
     * Keeps the game running by drawing canvas according to state of the game.
     *
     * @param currentNanoTime the nano time of the current game loop.
     */

    /**
     * Initializes the cameraVelocityY
     */
    private void initializeCameraVelocityY() {
        cameraVelocityY = GameController.PLAYER_Y_MARGIN - playerStartPositionY;
    }

    /**
     * Returns a list of Tiles for the selected map
     */
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Returns a list of Coins for the selected map
     */
    public List<Coin> getCoins() {
        return coins;
    }

    /**
     * Returns a list of Enemies for the selected map
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns a list of Enemies that needs to be safely removed from the game session
     */
    public List<Enemy> getDisposeEnemies() {
        return disposeEnemies;
    }

    /**
     * Returns a list of Bullets for the selected map
     */
    public List<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Returns a list of Bullets that needs to be safely removed from the game session
     */
    public List<Bullet> getDisposeBullets() {
        return disposeBullets;
    }

    /**
     * Returns a list of Bullets for the selected map
     */
    public List<Ammunition> getAmmunition() {
        return ammunition;
    }


    /**
     * Increases the variable coinCounter by one
     */
    public void addCoinCounter() {
        this.coinCounter++;
    }

    /**
     * Returns variable coinCounter
     */
    public int getCoinCounter() {
        return coinCounter;
    }


    /**
     * Increases the variable killCounter by one
     */
    public void addKillCounter() {
        this.killCounter++;
    }

    /**
     * Returns variable killCounter
     */
    public int getKillCounter() {
        return killCounter;
    }

    /**
     * Returns variable cameraStable
     */
    public boolean getCameraStable() {
        return cameraStable;
    }

    /**
     * Increases the enemy counter by 1
     */
    public void increaseEnemyCounter() {
        this.enemyAmount++;
    }

    /**
     * Gets the bullet counter
     *
     * @return bulletCounter
     */
    public int getBulletCounter() {
        return bulletCounter;
    }

    /**
     * Gets the wave number
     *
     * @return waveNr
     */
    public int getWaveNr() {
        return waveNr;
    }

    /**
     * Adds bullets to the bullet counter
     *
     * @param bullets the amount of bullets
     */
    public void addBulletCounter(int bullets) {
        bulletCounter += bullets;
    }

    /**
     * Decreases the bullet counter by 1
     */
    public void decreaseBulletCounter() {
        bulletCounter -= 1;
    }

    /**
     * Gets the player start position x
     *
     * @return playerStartPositionX
     */
    public int getPlayerStartPositionX() {
        return playerStartPositionX;
    }

    /**
     * Gets the player start position y
     *
     * @return playerStartPositionY
     */
    public int getPlayerStartPositionY() {
        return playerStartPositionY;
    }

    /**
     * Sets survival to true or false
     *
     * @param survival true if survival mode, false if not
     */
    public void setSurvival(boolean survival) {
        this.survival = survival;
    }

    /**
     * Sets the bullet counter
     *
     * @param bulletCounter the amount of bullets
     */
    public void setBulletCounter(int bulletCounter) {
        this.bulletCounter = bulletCounter;
    }

    /**
     * Returns true if survival mode, false if not
     *
     * @return survival
     */
    public boolean getSurvival() {
        return survival;
    }

    /**
     * Handles the camera velocity, renders the game. Spawns enemies if survival
     *
     * @param gc     the GraphicsContext
     * @param player the player object
     */
    public void tick(GraphicsContext gc, Player player) {
        handleCameraVelocity(player);
        render(gc, player);
        if (survival) spawnEnemies(player);
    }

    /**
     * Handles the camera velocity.
     * Makes sure player always has the same distance from the bottom of the application scene.
     *
     * @param player the player object
     */
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
                    cameraVelocityY = 20;
                } else {
                    cameraVelocityY = 0;
                    cameraStable = true;
                }
            } else {
                cameraVelocityY = 0;
            }
            playerChangeY += cameraVelocityY;
        }
    }

    /**
     * Gets the change in player position Y
     *
     * @return the change in player position Y
     */
    private int getPlayerChangeY() {
        return Math.abs(playerStartPositionY - playerChangeY + 190);
    }

    /**
     * Renders the GraphicsContext
     * Renders all objects on maps according to player position.
     *
     * @param gc     the GraphicsContext
     * @param player the player object
     */
    private void render(GraphicsContext gc, Player player) {
        player.setY(player.getY() + cameraVelocityY);
        renderTiles(gc, player);
        renderCoins(gc, player);
        renderEnemies(gc, player);
        renderBullets(gc, player);
        renderChest(gc, player);
        renderAmmunition(gc, player);


        if (!cameraInitialized) {
            player.setVelocityX(0, false);
            cameraInitialized = true;
        }
    }

    /**
     * Renders the tiles according to player position
     *
     * @param gc     the GraphicsContext
     * @param player the player objects
     */
    private void renderTiles(GraphicsContext gc, Player player) {
        for (Tile tile : getTiles()) {
            tile.setX(tile.getX() - player.getVelocityX());
            tile.setY(tile.getY() + cameraVelocityY);
            tile.tick(gc);
        }
    }

    /**
     * Renders the coins according to player position
     *
     * @param gc     the GraphicsContext
     * @param player the player objects
     */
    private void renderCoins(GraphicsContext gc, Player player) {
        for (Coin coin : getCoins()) {
            coin.setX(coin.getX() - player.getVelocityX());
            coin.setY(coin.getY() + cameraVelocityY);
            coin.tick(gc);
        }
    }

    /**
     * Renders the chest according to player position
     *
     * @param gc     the GraphicsContext
     * @param player the player objects
     */
    private void renderChest(GraphicsContext gc, Player player) {
        if (chest != null) {
            chest.setX(chest.getX() - player.getVelocityX());
            chest.setY(chest.getY() + cameraVelocityY);
            chest.tick(gc);
        }
    }

    /**
     * Renders the ammunition according to player position
     *
     * @param gc     the GraphicsContext
     * @param player the player objects
     */
    private void renderAmmunition(GraphicsContext gc, Player player) {
        for (Ammunition ammunition : getAmmunition()) {
            ammunition.setX(ammunition.getX() - player.getVelocityX());
            ammunition.setY(ammunition.getY() + cameraVelocityY);
            ammunition.tick(gc);
        }
    }

    /**
     * Renders the enemies according to player position
     *
     * @param gc     the GraphicsContext
     * @param player the player objects
     */
    private void renderEnemies(GraphicsContext gc, Player player) {
        for (Enemy enemy : getEnemies()) {

            if (survival) checkOutOfBounds(enemy);

            if (enemy.getX() < -enemy.getWidth() || enemy.getX() > GameController.CANVAS_WIDTH
                    || (enemy.getY() + enemy.getHeight() > GameController.CANVAS_HEIGHT)) enemyAttack = false;

            if (!enemy.getLeftCollision() && enemy.getX() > GameController.PLAYER_X_MARGIN && enemyAttack) {
                enemy.setVelocityX(-enemy.getSpeed(), false);
                enemy.setRightCollision(false);
            } else if (!enemy.getRightCollision() && enemy.getX() < GameController.PLAYER_X_MARGIN && enemyAttack) {
                enemy.setVelocityX(enemy.getSpeed(), false);
                enemy.setLeftCollision(false);
            } else {
                enemy.setVelocityX(0, false);
            }
            enemy.setX(enemy.getX() - player.getVelocityX());

            // If enemy collides with tileSide while falling
            if (enemy.getVelocityY() > 0) {
                enemy.setVelocityY(10);
                enemy.setRightCollision(false);
                enemy.setRightCollision(false);
            }

            enemy.setY(enemy.getY() + cameraVelocityY);

            enemy.handleSpriteStates();
            enemy.tick(gc);
            enemyAttack = true;
        }
        enemies.removeAll(disposeEnemies);
    }

    /**
     * Checks if an enemy spawns in illegal map position. Removes enemy and adds a new if true.
     *
     * @param enemy the enemy object
     */
    private void checkOutOfBounds(Enemy enemy) {
        if (enemy.getY() < -GameController.CANVAS_HEIGHT - GameController.PLAYER_Y_MARGIN
                || enemy.getY() > GameController.CANVAS_HEIGHT + GameController.PLAYER_Y_MARGIN * 2) {
            enemyAmount++;
            disposeEnemies.add(enemy);
        }
    }

    /**
     * Will spawn the specified enemyType at the spawnX and spawnY
     *
     * @param player the player objects
     * @param enemyType the EnemyType
     */
    private void spawnEnemyType(Player player, EnemyType enemyType){
        enemies.add(new Enemy(spawnX - player.getX() + GameController.PLAYER_X_MARGIN, spawnY - enemyType.getIdleH(), enemyType));
        enemyAmount--;
    }

    /**
     * Adds another wave of enemies in survival mode.
     *
     * @param player the player object
     */
    private void wave(Player player) {
        int spawnX;
        int spawnY;
        int random;

        waveNr++;
        enemyAmount = 10 * waveNr;
        killsRequired += enemyAmount;

        for (int i = 0; i < waveNr / 2; i++) {
            random = randomSpawn(player);
            spawnX = spawnSpots[0][random];
            spawnY = spawnSpots[1][random] - getPlayerChangeY() - GameController.TILE_SIZE;
            ammunition.add(new Ammunition(spawnX - player.getX() + GameController.PLAYER_X_MARGIN, spawnY));

        }
    }

    /**
     * Spawns a enemy in a random place of the map.
     *
     * @param player the player object
     * @return random spawn placement
     */
    private int randomSpawn(Player player) {
        int spawnX;
        while (!validSpawn) {
            random = ThreadLocalRandom.current().nextInt(0, spawnSpots[0].length);
            spawnX = spawnSpots[0][random];
            if (spawnX <= player.getX() - GameController.PLAYER_X_MARGIN || spawnX >= player.getX() + GameController.PLAYER_X_MARGIN)
                validSpawn = true;
        }
        validSpawn = false;
        return random;
    }

    /**
     * Spawns enemies in survival mode.
     *
     * @param player the player object
     */
    private void spawnEnemies(Player player) {
        if (enemyAmount == 0) {
            if (killCounter >= killsRequired) {
                wave(player);
            }
        } else if (cameraVelocityY == 0 && (player.isRunning() || player.isIdling())) {
            int random = randomSpawn(player);
            spawnX = spawnSpots[0][random];
            spawnY = spawnSpots[1][random];

            if (waveNr > 1) {
                spawnY -= getPlayerChangeY();
            }

            if (enemyAmount <= 20 && enemyAmount > 0) {
                spawnEnemyType(player,EnemyType.ENEMY_I);
            } else if (enemyAmount <= 40 && enemyAmount > 20 && cameraVelocityY == 0) {
                spawnEnemyType(player,EnemyType.ENEMY_B);
            } else if (enemyAmount <= 60 && enemyAmount > 40 && cameraVelocityY == 0) {
                spawnEnemyType(player,EnemyType.ENEMY_C);
            } else if (enemyAmount <= 80 && enemyAmount > 60 && cameraVelocityY == 0) {
                spawnEnemyType(player,EnemyType.ENEMY_D);
            } else if (enemyAmount <= 100 && enemyAmount > 80 && cameraVelocityY == 0) {
                spawnEnemyType(player,EnemyType.ENEMY_E);
            } else if (enemyAmount <= 120 && enemyAmount > 100 && cameraVelocityY == 0) {
                spawnEnemyType(player,EnemyType.ENEMY_F);
            } else if (enemyAmount <= 140 && enemyAmount > 120 && cameraVelocityY == 0) {
                spawnEnemyType(player,EnemyType.ENEMY_G);
            } else if (enemyAmount <= 160 && enemyAmount > 140 && cameraVelocityY == 0) {
                spawnEnemyType(player,EnemyType.ENEMY_H);
            } else if (enemyAmount <= 180 && enemyAmount > 160 && cameraVelocityY == 0) {
                spawnEnemyType(player,EnemyType.ENEMY_I);
            }
        }
    }


    /**
     * Renders the bullets according to player position
     *
     * @param gc     the GraphicsContext
     * @param player the player objects
     */
    private void renderBullets(GraphicsContext gc, Player player) {
        Iterator<Bullet> iterator = getBullets().iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.setY(bullet.getY() + cameraVelocityY);
            if (bullet.getFacingRight()) {
                bullet.setVelocityX(BulletType.valueOf(MainMenuController.selectedBullet).getSpeed(), true);
                bullet.setX(bullet.getX() - player.getVelocityX());
                if (player.getVelocityX() >= 0) {
                    bullet.setX(bullet.getX() + player.getVelocityX());
                }

            } else {
                bullet.setVelocityX(-BulletType.valueOf(MainMenuController.selectedBullet).getSpeed(), true);
                bullet.setX(bullet.getX() - player.getVelocityX());
            }
            if (bullet.getX() > GameController.CANVAS_WIDTH || bullet.getX() < 0) {
                disposeBullets.add(bullet);
            }
            bullet.tick(gc);
        }
        bullets.removeAll(disposeBullets);
    }


    /**
     * Adds a bullet to the bullet list
     *
     * @param b the bullet
     */
    public void addBullet(Bullet b) {
        bullets.add(b);
    }

    /**
     * Returns the amount of the current type in map
     *
     * @param type     the type
     * @param fileName the map name
     * @return amount of the current type
     */
    private int getNumberOfType(char type, String fileName) {
        Scanner scanner = null;
        int numberOfType = 0;
        InputStream inputStream = new JarUtil().getFileFromJar(fileName);

        if (inputStream == null) {
            inputStream = getClass().getResourceAsStream(fileName);
        }

        try {
            if (inputStream == null) {
                File file = new File(fileName);
                inputStream = new FileInputStream(file);
            }
            scanner = new Scanner(inputStream, "utf-8");
            while (scanner.hasNext()) {
                char[] line = scanner.nextLine().toLowerCase().toCharArray();

                for (Character character : line) {
                    if (character.equals(type)) {
                        numberOfType++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        scanner.close();
        return numberOfType;


    }

    /**
     * Parses the map given from the char array.
     * Loops through every element in the array, and adds objects to the different objects lists given by custom rules.
     *
     * @param map the map represented by as char array.
     */
    private void parseMap(char[][] map) {
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
                        TileType tileType = getTileType(map, y, x);
                        tiles.add(new Tile(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE, tileType));

                        setSpikeTilesVariables(x, y);
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
                        spawnSpots[1][spawnIndex] = y * GameController.TILE_SIZE;
                        spawnIndex++;
                        break;
                    case (END):
                        chest = new Chest(x * GameController.TILE_SIZE, y * GameController.TILE_SIZE);
                }
            }
        }

        setSpikeTiles();
    }

    /**
     * Get TileType based on neighbour tiles
     *
     * @param map 2d array
     * @param y   index
     * @param x   index
     * @return tileType
     */
    public TileType getTileType(char[][] map, int y, int x) {
        if (y > 0 && map[y - 1][x] == TILE) {
            //Dirt
            if ((y < map.length - 1 && map[y + 1][x] != TILE) || y == map.length - 1) {
                // Bottom dirt
                if (((x > 0 && map[y][x - 1] == TILE)) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                    // Bottom dirt with tiles on both sides
                    return TileType.DIRT_BOTTOM;
                } else if ((x > 0 && map[y][x - 1] == TILE || x == 0) && (x < map[y].length - 1 && map[y][x + 1] != TILE)) {
                    // Bottom dirt with tile on left
                    return TileType.DIRT_RIGHT_CORNER;
                } else if ((x > 0 && map[y][x - 1] != TILE || x == 0) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                    // Bottom dirt with tile on right
                    return TileType.DIRT_LEFT_CORNER;
                } else {
                    return TileType.DIRT_COLUMN;
                }
            } else if ((x > 0 && map[y][x - 1] != TILE || x == 0) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                // Not bottom dirt with tile on right
                return TileType.DIRT_LEFT;
            } else if ((x > 0 && map[y][x - 1] == TILE) && (x < map[y].length - 1 && map[y][x + 1] != TILE)) {
                return TileType.DIRT_RIGHT;
            } else if ((x > 0 && map[y][x - 1] != TILE) && (x < map[y].length - 1 && map[y][x + 1] != TILE)) {
                return TileType.DIRT_COLUMN;
            } else {
                return TileType.DIRT;
            }
        } else {
            //Grass
            if ((x > 0 && map[y][x - 1] != TILE || x == 0) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                // Grass tile on right
                return TileType.GRASS_LEFT;
            } else if ((x > 0 && map[y][x - 1] == TILE) && (x < map[y].length - 1 && map[y][x + 1] != TILE)) {
                return TileType.GRASS_RIGHT;
            } else if ((x > 0 && map[y][x - 1] == TILE) && (x < map[y].length - 1 && map[y][x + 1] == TILE)) {
                return TileType.GRASS_MID;
            }
        }

        return TileType.GRASS_TOP;
    }

    /**
     * Sets the spike tiles below the map which will kill you on intersection.
     */
    private void setSpikeTiles() {
        int marginX = 30;
        for (int x = minX - marginX; x <= maxX + marginX; x++) {
            tiles.add(new Tile(x * GameController.TILE_SIZE, (maxY + 3) * GameController.TILE_SIZE, TileType.SPIKE_UP));
        }
    }

    /**
     * Sets the variables needed to place the spike tiles.
     *
     * @param x the x position
     * @param y the y position
     */
    private void setSpikeTilesVariables(int x, int y) {
        if (x <= minX) {
            minX = x;
        }
        if (x >= maxX) {
            maxX = x;
        }
        if (y >= maxY) {
            maxY = y;
        }
    }

    /**
     * Loads a map given by the map name
     *
     * @param fileName the map name
     * @return a char array represented as the map
     */
    private char[][] loadMap(String fileName) {
        InputStream inputStream = new JarUtil().getFileFromJar(fileName);

        if (inputStream == null) {
            inputStream = getClass().getResourceAsStream(fileName);
        }

        try {
            if (inputStream == null) {
                File file = new File(fileName);
                inputStream = new FileInputStream(file);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        char[][] map = mapParser.getArrayFromInputStream(inputStream);

        if (map == null) {
            try {
                throw new InvalidMapException("Invalid map file");
            } catch (InvalidMapException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * Gets the chest object
     *
     * @return chest
     */
    public Chest getChest() {
        return chest;
    }
}
