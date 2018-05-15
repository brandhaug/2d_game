package Game;

import Game.GameObjects.*;
import Game.Levels.Level;
import Resources.soundEffects.SoundEffects;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class CollisionHandler {
    private Player player;
    private Level level;
    private List<Bullet> disposeBullets;
    private List<Enemy> disposeEnemies;
    static boolean playerEnemyCollision = true;
    public static int killCoins;

    /**
     * Sets the variables in class from parameters.
     *
     * @param player the Player object
     * @param level  the Level object
     */
    CollisionHandler(Player player, Level level) {
        this.player = player;
        this.level = level;
        disposeBullets = level.getDisposeBullets();
        disposeEnemies = level.getDisposeEnemies();
    }

    /**
     * Is called on in every tick from game loop. Handles all collisions.
     */
    void tick() {
        handleTileCollision();
        handleCoinCollision();
        handleEnemyCollision();
        handleChestCollision();
        handleAmmunitionCollision();
    }

    /**
     * Remove coins from list and increments coin counter on collision.
     */
    private void handleCoinCollision() {
        Iterator<Coin> coinIterator = level.getCoins().iterator();
        while (coinIterator.hasNext()) {
            Coin coin = coinIterator.next();
            if (intersectsWithPlayer(coin) && level.getCameraStable()) {
                coinIterator.remove();
                level.addCoinCounter();
                SoundEffects.COIN.play();
            }
        }
    }

    /**
     * Animates chest on collision.
     */
    private void handleChestCollision() {
        Chest chest = level.getChest();
        if (chest != null && intersectsWithPlayer(chest)) {
            chest.animateChest();
        }
    }

    /**
     * Handles tile collision by calling on sub methods top, bottom, right and left collision.
     * Increments velocityY by 2 if no bottom collision.
     */
    private void handleTileCollision() {
        player.setLeftCollision(false);
        player.setRightCollision(false);
        player.setVelocityY(player.getVelocityY() + 2);

        for (Tile tile : level.getTiles()) {
            handleEnemyTileCollision(tile);
            handleTileBulletCollision(tile);

            if (player.getBoundsBottom().intersects(tile.getBoundsTop()) && !player.isJumping()) {
                handleTileTopCollision(player);
            }
            if (player.getBoundsTop().intersects(tile.getBoundsBottom()) && !player.isFalling()) {
                handleTileBottomCollision(player);
            }
            if (player.getBoundsLeft().intersects(tile.getBoundsRight()) && !player.getLastSpriteRight()) {
                handleTileRightCollision(player);
            }
            if (player.getBoundsRight().intersects(tile.getBoundsLeft()) && player.getLastSpriteRight()) {
                handleTileLeftCollision(player);
            }

            if (player.getBoundsBottom().intersects(tile.getBoundsTop()) && tile.getTileType() == TileType.SPIKE_UP) {
                player.setAlive(false);
            }
        }
    }

    /**
     * Handles enemy collision with tile by calling on sub methods top, bottom, right and left collision.
     *
     * @param tile the Tile object
     * @see Tile
     */
    private void handleEnemyTileCollision(Tile tile) {
        for (Enemy enemy : level.getEnemies()) {

            if (enemy.getBoundsBottom().intersects(tile.getBoundsTop())) {
                handleTileTopCollision(enemy);
            }

            if (enemy.getBoundsBottom().intersects(tile.getBoundsTop()) && tile.getTileType() == TileType.SPIKE_UP) {
                level.increaseEnemyCounter();
                disposeEnemies.add(enemy);
            }

            if (enemy.getBoundsTop().intersects(tile.getBoundsBottom())) {
                handleTileBottomCollision(enemy);
            }

            if (enemy.getBoundsLeft().intersects(tile.getBoundsRight())) {
                handleTileRightCollision(enemy);
            }

            if (enemy.getBoundsRight().intersects(tile.getBoundsLeft())) {
                handleTileLeftCollision(enemy);
            }
        }
    }

    /**
     * Handles the collision that happens when a bullet hits a tile.
     * Adds bullet to a list of bullets to be removed on collision.
     * @param tile the tile
     */
    private void handleTileBulletCollision(Tile tile) {
        for (Bullet bullet : level.getBullets()) {

            if (bullet.getBoundsBottom().intersects(tile.getBoundsLeft())) {
                disposeBullets.add(bullet);
            }

            if (bullet.getBoundsBottom().intersects(tile.getBoundsRight())) {
                disposeBullets.add(bullet);
            }

            if (bullet.getBoundsTop().intersects(tile.getBoundsRight())) {
                disposeBullets.add(bullet);
            }

            if (bullet.getBoundsTop().intersects(tile.getBoundsLeft())) {
                disposeBullets.add(bullet);
            }
        }
    }

    /**
     * Handles the collision when a GameObject collides with the top of a tile.
     *
     * @param gameObject the GameObject object.
     * @see GameObject
     */
    private void handleTileTopCollision(GameObject gameObject) {
        gameObject.setVelocityY(0);
    }

    /**
     * Handles the collision when a GameObject collides with the bottom of a tile.
     *
     * @param gameObject the GameObject object.
     * @see GameObject
     */
    private void handleTileBottomCollision(GameObject gameObject) {
        gameObject.setVelocityY(1);
    }

    /**
     * Handles the collision when a GameObject collides with the right side of a tile.
     *
     * @param gameObject the GameObject object.
     * @see GameObject
     */
    private void handleTileRightCollision(GameObject gameObject) {
        if (gameObject.getVelocityX() != 0) {
            gameObject.setLeftCollision(true);
            gameObject.setVelocityX(0, false);
        }
    }

    /**
     * Handles the collision when a GameObject collides with the left side of a tile.
     *
     * @param gameObject the GameObject object.
     * @see GameObject
     */
    private void handleTileLeftCollision(GameObject gameObject) {
        if (gameObject.getVelocityX() != 0) {
            gameObject.setRightCollision(true);
            gameObject.setVelocityX(0, false);
        }
    }

    /**
     * Handles the collision that happens when a bullet or player hits an enemy, by calling on sub methods.
     */
    private void handleEnemyCollision() {
        Iterator<Enemy> iterator = level.getEnemies().iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();

            handleEnemyBulletCollision(enemy, iterator);

            if (playerEnemyCollision) {
                if (enemy.getBoundsTop().intersects(player.getBoundsBottom())) {
                    handleEnemyTopCollision(enemy.getDamage(), enemy, iterator);
                }

                if (enemy.getBoundsLeft().intersects(player.getBoundsRight())
                        || enemy.getBoundsLeft().intersects(player.getBoundsLeft()) ||
                        enemy.getBoundsRight().intersects(player.getBoundsLeft()) ||
                        enemy.getBoundsRight().intersects(player.getBoundsRight()) ||
                        enemy.getBoundsBottom().intersects(player.getBoundsTop())
                        ) {
                    handleEnemyCollision(enemy.getDamage());
                }
            }
        }
    }

    /**
     * Handles the collision that happens when a bullet hits an enemy.
     * Removes bullet from player inventory. Decreases HP on enemy.
     * Increases kill coins if enemy dies, as well as removing enemy.
     * @param enemy the enemy
     * @param enemyIterator an iterator of the enemies on map
     */
    private void handleEnemyBulletCollision(Enemy enemy, Iterator<Enemy> enemyIterator) {
        for (Bullet bullet : level.getBullets()) {
            if (bullet.getBoundsTop().intersects(enemy.getBoundsLeft()) || bullet.getBoundsTop().intersects(enemy.getBoundsRight())
                    || bullet.getBoundsBottom().intersects(enemy.getBoundsLeft()) || bullet.getBoundsBottom().intersects(enemy.getBoundsRight())) {
                disposeBullets.add(bullet);
                if (enemy.getHp() <= bullet.getDamage()) {
                    enemy.setAlive(false);
                    killCoins += enemy.getPoints();
                    level.addKillCounter();
                    SoundEffects.ENEMY_DEATH.play();
                    disposeEnemies.add(enemy);
                } else {
                    enemy.setHp(enemy.getHp() - bullet.getDamage());
                    enemyIsHit(enemy);
                }
            }
        }
    }

    /**
     * Sets the enemy to hit. Moving it's y position.
     * @param enemy the enemy that is hit
     */
    private void enemyIsHit(Enemy enemy) {
        enemy.setEnemyHit(true);
        enemy.setY(enemy.getY() - (enemy.getHeightHit() - enemy.getHeight()));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                enemy.setEnemyHit(false);
                timer.cancel();
            }
        }, 700);
    }

    /**
     * Sets player and enemy collision to true for 0.3 seconds using Timer. Will make player unavailable to jump for 0.3 seconds.
     * @see Timer
     */
    private void playerHitTimeOut() {
        playerEnemyCollision = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                playerEnemyCollision = true;
                timer.cancel();
            }
        }, 300);
    }

    /**
     * Handles the collision that happens when a player hits the top of an enemy.
     * Adds kill points and removes enemy if it dies, decreases its hp if not.
     * @param enemyDamage the damage done to the enemy
     * @param enemy the enemy that is hit
     * @param enemyIterator an enemy iterator of all enemies on the map
     */
    private void handleEnemyTopCollision(int enemyDamage, Enemy enemy, Iterator<Enemy> enemyIterator) {
        playerHit(enemyDamage, false);
        if (enemy.getHp() == 1) {
            level.addKillCounter();
            killCoins += enemy.getPoints();
            SoundEffects.ENEMY_DEATH.play();
            enemyIterator.remove();
        } else {
            enemy.setHp(enemy.getHp() - 1);
            enemyIsHit(enemy);
        }
    }

    /**
     * Handles the collision that happens when player hits an enemy (not on top).
     * Decreases the damage of the player, and makes player unavailable of jumping for a few milliseconds.
     * @param enemyDamage the damage done by the enemy
     */
    private void handleEnemyCollision(int enemyDamage) {
        playerHit(enemyDamage, true);
        playerHitTimeOut();
    }

    /**
     * Decreases players HP by the enemy damage done. Sets player to dead if HP is below 0.
     * EnemyHit is 10 times more in Classic Mode
     * Sets velocityY to -10.
     * @param enemyDamage the damage done by the enemy
     * @param damage true if damage should be taken, will only set velocityY if false.
     */
    private void playerHit(int enemyDamage, boolean damage) {
        player.setVelocityY(-10);
        if (damage) {
            SoundEffects.HIT.play();
            if(level.getSurvival()){
                player.setHp(player.getHp() - enemyDamage);
            }else{
                player.setHp(player.getHp() - enemyDamage*10);
            }
            if (player.getHp() <= 0) player.setAlive(false);
        }
    }

    /**
     * Handles the collision that happens between a player and ammunition in survival mode.
     * Adds bullets to players inventory on collision.
     */
    private void handleAmmunitionCollision() {
        Iterator<Ammunition> ammunitionIterator = level.getAmmunition().iterator();
        while (ammunitionIterator.hasNext()) {
            Ammunition ammo = ammunitionIterator.next();
            if (intersectsWithPlayer(ammo)) {
                level.addBulletCounter(20);
                ammunitionIterator.remove();
            }
        }
    }

    /**
     * Checks if a game object intersects with player.
     * @param object the game object
     * @return true if intersection is made, false if not.
     */
    private boolean intersectsWithPlayer(GameObject object) {
        return topIntersectsWithPlayer(object) || bottomIntersectsWithPlayer(object) || rightIntersectsWithPlayer(object) || leftIntersectsWithPlayer(object);
    }

    /**
     * Checks if the top of a game object intersects with player.
     * @param object the game object
     * @return true if intersection is made, false if not.
     */
    private boolean topIntersectsWithPlayer(GameObject object) {
        return object.getBoundsTop().intersects(player.getBoundsTop()) || object.getBoundsTop().intersects(player.getBoundsRight())
                || object.getBoundsTop().intersects(player.getBoundsBottom()) || object.getBoundsTop().intersects(player.getBoundsLeft());
    }

    /**
     * Checks if the bottom of a game object intersects with player.
     * @param object the game object
     * @return true if intersection is made, false if not.
     */
    private boolean bottomIntersectsWithPlayer(GameObject object) {
        return object.getBoundsBottom().intersects(player.getBoundsTop()) || object.getBoundsBottom().intersects(player.getBoundsRight())
                || object.getBoundsBottom().intersects(player.getBoundsBottom()) || object.getBoundsBottom().intersects(player.getBoundsLeft());
    }

    /**
     * Checks if the right side of a game object intersects with player.
     * @param object the game object
     * @return true if intersection is made, false if not.
     */
    private boolean rightIntersectsWithPlayer(GameObject object) {
        return object.getBoundsRight().intersects(player.getBoundsTop()) || object.getBoundsRight().intersects(player.getBoundsRight())
                || object.getBoundsRight().intersects(player.getBoundsBottom()) || object.getBoundsRight().intersects(player.getBoundsLeft());
    }

    /**
     * Checks if the left side of a game object intersects with player.
     * @param object the game object
     * @return true if intersection is made, false if not.
     */
    private boolean leftIntersectsWithPlayer(GameObject object) {
        return object.getBoundsLeft().intersects(player.getBoundsTop()) || object.getBoundsLeft().intersects(player.getBoundsRight())
                || object.getBoundsLeft().intersects(player.getBoundsBottom()) || object.getBoundsLeft().intersects(player.getBoundsLeft());
    }
}