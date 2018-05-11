package Game;

import Game.GameObjects.*;
import Game.Levels.Level;
import Resources.soundEffects.SoundEffects;

import java.util.*;


public class CollisionHandler {
    private Player player;
    private Level level;
    private List<Bullet> disposeBullets;
    private List<Enemy> disposeEnemies;
    public static boolean playerEnemyCollision = true;
    static int killcoins;

    /**
     * Sets the variables in class from parameters.
     * @param player the Player object
     * @param level the Level object
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
    public void tick() {
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
            if (intersectsWithPlayer(coin)) {
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
                //System.out.println("TILE Y: " + tile.getY()/GameController.TILE_SIZE);
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
     * @param tile the Tile object
     * @see Tile
     */
    private void handleEnemyTileCollision(Tile tile) {
        for (Enemy enemy : level.getEnemies()) {

            if (enemy.getBoundsBottom().intersects(tile.getBoundsTop())) {
                handleTileTopCollision(enemy);
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
     * @param gameObject the GameObject object.
     * @see GameObject
     */
    private void handleTileTopCollision(GameObject gameObject) {
        //player.setY(tileY - player.getCurrentSpriteSheet().getSpriteHeight() + 10);

        //Find better sound effect before using this method
        /*
        if(player.getCurrentSpriteState() == Player.PLAYER_FALLING_LEFT || player.getCurrentSpriteState() == Player.PLAYER_FALLING_RIGHT){
            soundEffects.LANDING.play();
        }
        */
        gameObject.setVelocityY(0);
        gameObject.setFirstCollision();
    }

    /**
     * Handles the collision when a GameObject collides with the bottom of a tile.
     * @param gameObject the GameObject object.
     * @see GameObject
     */
    private void handleTileBottomCollision(GameObject gameObject) {
        //player.setY(tileY + tileHeight);
        gameObject.setVelocityY(1);
    }

    /**
     * Handles the collision when a GameObject collides with the right side of a tile.
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
     * @param gameObject the GameObject object.
     * @see GameObject
     */
    private void handleTileLeftCollision(GameObject gameObject) {
        if (gameObject.getVelocityX() != 0) {
            gameObject.setRightCollision(true);
            gameObject.setVelocityX(0, false);
        }
    }

    public void handleEnemyCollision() {
        Iterator<Enemy> iterator = level.getEnemies().iterator();
            while (iterator.hasNext()) {
                Enemy e = iterator.next();

                handleEnemyBulletCollision(e, iterator);

                if (playerEnemyCollision) {
                    if (e.getBoundsTop().intersects(player.getBoundsBottom())) {
                        handleEnemyTopCollision(e.getDamage(), e, iterator);
                    }

                    if (e.getBoundsRight().intersects(player.getBoundsLeft())) {
                        handleEnemyRightCollision(e.getDamage());
                    }

                    if (e.getBoundsBottom().intersects(player.getBoundsTop())) {
                        handleEnemyBottomCollision(e.getDamage());
                    }

                    if (e.getBoundsLeft().intersects(player.getBoundsRight())) {
                        handleEnemyLeftCollision(e.getDamage());
                    }
                }
            }
        }

    private void handleEnemyBulletCollision(Enemy enemy, Iterator<Enemy> enemyIterator) {
        for (Bullet bullet : level.getBullets()) {
            if (bullet.getBoundsTop().intersects(enemy.getBoundsLeft()) || bullet.getBoundsTop().intersects(enemy.getBoundsRight())
                    || bullet.getBoundsBottom().intersects(enemy.getBoundsLeft()) || bullet.getBoundsBottom().intersects(enemy.getBoundsRight())) {
                disposeBullets.add(bullet);
                if(enemy.getHp() <= bullet.getDamage()) {
                    enemy.setAlive(false);
                    killcoins += enemy.getPoints();
                    level.addKillCounter();
                    SoundEffects.ENEMY_DEATH.play();
                    disposeEnemies.add(enemy);
                }else {
                    enemy.setHp(enemy.getHp()-bullet.getDamage());
                    enemyIsHit(enemy);
                }
            }
        }
    }

    private void enemyIsHit(Enemy enemy){
        enemy.setEnemyHit(true);
        enemy.setY(enemy.getY() - (enemy.getHeightHit()-enemy.getHeight()));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                enemy.setEnemyHit(false);
                timer.cancel();
            }
        }, 700);
    }


    private void playerHitTimeOut(){
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

    public void handleEnemyTopCollision(int enemyDamage, Enemy enemy, Iterator<Enemy> enemyIterator) {
        playerHit(enemyDamage,false);
        if(enemy.getHp() == 1) {
            level.addKillCounter();
            killcoins += enemy.getPoints();
            SoundEffects.ENEMY_DEATH.play();
            enemyIterator.remove();
        }else{
            enemy.setHp(enemy.getHp()-1);
            enemyIsHit(enemy);
        }
    }

    private void handleEnemyBottomCollision(int enemyDamage) {
        playerHit(enemyDamage,true);
        playerHitTimeOut();
    }

    private void handleEnemyRightCollision(int enemyDamage) {
        playerHitTimeOut();
        playerHit(enemyDamage,true);
    }
    private void handleEnemyLeftCollision(int enemyDamage) {
        playerHitTimeOut();
        playerHit(enemyDamage,true);
    }

    private void playerHit(int enemyDamage, boolean damage) {
        player.setVelocityY(-10);
        if (damage) {
            SoundEffects.HIT.play();
            player.setHp(player.getHp() - enemyDamage);
            if (player.getHp() <= 0) player.setAlive(false);
        }
    }

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

    private boolean intersectsWithPlayer(GameObject object) {
        return topIntersectsWithPlayer(object) || bottomIntersectsWithPlayer(object) || rightIntersectsWithPlayer(object) || leftIntersectsWithPlayer(object);
    }

    private boolean topIntersectsWithPlayer(GameObject object) {
        return object.getBoundsTop().intersects(player.getBoundsTop()) || object.getBoundsTop().intersects(player.getBoundsRight())
                || object.getBoundsTop().intersects(player.getBoundsBottom()) || object.getBoundsTop().intersects(player.getBoundsLeft());
    }

    private boolean bottomIntersectsWithPlayer(GameObject object) {
        return object.getBoundsBottom().intersects(player.getBoundsTop()) || object.getBoundsBottom().intersects(player.getBoundsRight())
                || object.getBoundsBottom().intersects(player.getBoundsBottom()) || object.getBoundsBottom().intersects(player.getBoundsLeft());
    }

    private boolean rightIntersectsWithPlayer(GameObject object) {
        return object.getBoundsRight().intersects(player.getBoundsTop()) || object.getBoundsRight().intersects(player.getBoundsRight())
                || object.getBoundsRight().intersects(player.getBoundsBottom()) || object.getBoundsRight().intersects(player.getBoundsLeft());
    }

    private boolean leftIntersectsWithPlayer(GameObject object) {
        return object.getBoundsLeft().intersects(player.getBoundsTop()) || object.getBoundsLeft().intersects(player.getBoundsRight())
                || object.getBoundsLeft().intersects(player.getBoundsBottom()) || object.getBoundsLeft().intersects(player.getBoundsLeft());
    }
}