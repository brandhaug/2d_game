package Game;

import Game.GameObjects.*;
import Game.Levels.Level;
import Resources.soundEffects.SoundEffects;

import java.lang.management.PlatformLoggingMXBean;
import java.util.*;


public class CollisionHandler {
    private Player player;
    private Level level;
    private List<Bullet> disposeBullets;

    private boolean bouncing = false;
    private boolean collisionOn = true;

    /**
     * Sets the variables in class from parameters.
     * @param player the Player object
     * @param level the Level object
     */
    CollisionHandler(Player player, Level level) {
        this.player = player;
        this.level = level;
        disposeBullets = level.getDisposeBullets();
    }

    /**
     * Is called on in every tick from game loop. Handles all collisions.
     */
    public void tick() {
        handleTileCollision();
        handleCoinCollision();
        handleEnemyCollision();
        handleChestCollision();
    }

    /**
     * Remove coins from list and increments coin counter on collision.
     */
    private void handleCoinCollision() {
        List<Coin> coins = level.getCoins();
        for (Coin coin : coins) {
            if (intersectsWithPlayer(coin)) {
                coins.remove(coin);
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
        if (intersectsWithPlayer(chest)) {
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
        }
    }

    /**
     * Handles enemy collision with tile by calling on sub methods top, bottom, right and left collision.
     * @param tile the Tile object
     * @see Tile
     */
    private void handleEnemyTileCollision(Tile tile) {
        for (Enemy enemy : level.getEnemies()) {
            if (enemy.getBoundsBottom().intersects(tile.getBoundsTop()) && !player.isJumping()) {
                handleTileTopCollision(enemy);
            }
            if (enemy.getBoundsTop().intersects(tile.getBoundsBottom()) && !player.isFalling()) {
                handleTileBottomCollision(enemy);
            }
            if (enemy.getBoundsLeft().intersects(tile.getBoundsRight()) && !enemy.getLastSpriteRight()) {
                handleTileRightCollision(enemy);
            }
            if (enemy.getBoundsRight().intersects(tile.getBoundsLeft()) && enemy.getLastSpriteRight()) {
                handleTileLeftCollision(enemy);
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
        bouncing = false;
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

    private void handleEnemyCollision() {
        Iterator<Enemy> iterator = level.getEnemies().iterator();
        if (collisionOn) {
            if (bouncing && player.getCurrentSpriteState() > 3) player.setVelocityX(0, false);

            while (iterator.hasNext()) {
                Enemy enemy = iterator.next();
                handleEnemyBulletCollision(enemy, iterator);

                if (enemy.getBoundsTop().intersects(player.getBoundsBottom())) {
                    handleEnemyTopCollision(enemy.getDamage());
                }
                if (enemy.getBoundsRight().intersects(player.getBoundsLeft())) {
                    handleEnemyRightCollision(enemy.getDamage());
                }
                if (enemy.getBoundsBottom().intersects(player.getBoundsTop())) {
                    handleEnemyBottomCollision(enemy.getDamage());
                }
                if (enemy.getBoundsLeft().intersects(player.getBoundsRight())) {
                    handleEnemyLeftCollision(enemy.getDamage());
                }
            }
        }
    }

    private void handleEnemyBulletCollision(Enemy enemy, Iterator<Enemy> enemyIterator) {

        for (Bullet bullet : level.getBullets()) {
            //System.out.println(bullet.getX());
            if (bullet.getBoundsTop().intersects(enemy.getBoundsLeft()) || bullet.getBoundsBottom().intersects(enemy.getBoundsLeft())) {
                disposeBullets.add(bullet);
                if (enemy.getHp() <= bullet.getDamage()) {
                    enemy.setAlive(false);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            enemyIterator.remove();
                            timer.cancel();
                        }
                    }, 150);
                } else {
                    enemy.setHp(bullet.getDamage());
                    enemy.setEnemyhit(true);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            enemy.setEnemyhit(false);
                            //enemy.setY(enemy.getY() - 40);
                            timer.cancel();
                        }
                    }, 500);
                }
            }
        }
    }


    public void handleEnemyTopCollision(int enemyDamage) {
        bouncing = true;
        if (player.getCurrentSpriteState() == Player.PLAYER_FALLING_RIGHT || player.getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT
                || player.getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT) {
            player.setVelocityY(-25);
            player.setVelocityX(20, false);
        } else {
            player.setVelocityY(-25);
            player.setVelocityX(-20, false);
        }
        playerHit(enemyDamage);
    }


    private void handleEnemyBottomCollision(int enemyDamage) {
        bouncing = true;
        playerHit(enemyDamage);
        if (player.isJumping()) {
            player.setVelocityY(20);
        }

        if (player.getCurrentSpriteState() == Player.PLAYER_IDLING_LEFT || player.getCurrentSpriteState() == Player.PLAYER_RUNNING_LEFT) {
            collisionOn = false;
            player.setVelocityY(-20);
        }

        if (player.getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT || player.getCurrentSpriteState() == Player.PLAYER_RUNNING_RIGHT) {
            collisionOn = false;
            player.setVelocityY(-20);
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                collisionOn = true;
                timer.cancel();
            }
        }, 1000);
    }

    private void handleEnemyRightCollision(int enemyDamage) {
        bouncing = true;
        player.setVelocityX(10, false);
        player.setVelocityY(-10);
        playerHit(enemyDamage);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.setVelocityX(0, false);
                timer.cancel();
            }
        }, 150);
    }

    private void handleEnemyLeftCollision(int enemyDamage) {
        bouncing = true;
        player.setVelocityX(-10, false);
        player.setVelocityY(-10);
        playerHit(enemyDamage);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.setVelocityX(0, false);
                timer.cancel();
            }
        }, 200);
    }

    private void playerHit(int enemyDamage) {
        SoundEffects.HIT.play();
        player.setHp(player.getHp() - enemyDamage);
        if (player.getHp() == 0) player.setAlive(false);
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