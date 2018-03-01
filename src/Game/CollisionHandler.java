package Game;

import Game.GameObjects.*;
import Game.Levels.Level;
import Resources.soundEffects.SoundEffects;

import java.lang.management.PlatformLoggingMXBean;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


public class CollisionHandler {
    private Player player;
    private Level level;
    private SoundEffects soundEffects;

    private boolean bouncing = false;
    private boolean collisionOn = true;


    public CollisionHandler(Player player, Level level, SoundEffects soundEffects) {
        this.player = player;
        this.level = level;
        this.soundEffects = soundEffects;

    }

    public void tick() {
        handleTileCollision();
        handleCoinCollision();
        handleEnemyCollision();
    }

    public void handleCoinCollision() {
        Iterator<Coin> iterator = level.getCoins().iterator();

        while (iterator.hasNext()) {
            Coin c = iterator.next();
            if (c.getBoundsBottom().intersects(player.getBoundsTop()) || c.getBoundsBottom().intersects(player.getBoundsRight())
                    || c.getBoundsBottom().intersects(player.getBoundsBottom()) || c.getBoundsBottom().intersects(player.getBoundsLeft())) {
                iterator.remove();
                level.addCoinCounter();
                SoundEffects.COIN.play();
            }
        }
    }

    public void handleTileCollision() {
        player.setLeftCollision(false);
        player.setRightCollision(false);

        player.setVelocityY(player.getVelocityY() + 2);

        for (Tile tile : level.getTiles()) {
            handleEnemyTileCollision(tile);
            if (player.getBoundsBottom().intersects(tile.getBoundsTop()) && player.getCurrentSpriteState() != Player.PLAYER_JUMPING_RIGHT && player.getCurrentSpriteState() != Player.PLAYER_JUMPING_LEFT) {
                handleTileTopCollision(player);
            }

            if (player.getBoundsTop().intersects(tile.getBoundsBottom()) && player.getCurrentSpriteState() != Player.PLAYER_FALLING_LEFT && player.getCurrentSpriteState() != Player.PLAYER_FALLING_RIGHT) {
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

    int hitcounterLEFT = 0;
    int hitcounterRIGHT = 0;

    public void handleEnemyTileCollision(Tile tile) {

        for (Enemy enemy : level.getEnemies()) {

            if (enemy.getBoundsBottom().intersects(tile.getBoundsTop()) && enemy.getCurrentSpriteState() != Enemy.ENEMY_JUMPING_RIGHT && enemy.getCurrentSpriteState() != Enemy.ENEMY_JUMPING_LEFT) {
                handleTileTopCollision(enemy);
            }

            if (enemy.getBoundsTop().intersects(tile.getBoundsBottom()) && enemy.getCurrentSpriteState() != Enemy.ENEMY_FALLING_LEFT && enemy.getCurrentSpriteState() != Enemy.ENEMY_FALLING_RIGHT) {
                handleTileBottomCollision(enemy);
            }

            if (enemy.getBoundsLeft().intersects(tile.getBoundsRight()) && !enemy.getLastSpriteRight()) {
                handleTileRightCollision(enemy);
                if (enemy.getVelocityX() != 0) {
                    enemy.setLeftCollision(true);
                    enemy.setVelocityX(0, false);
                }
            }

            if (enemy.getBoundsRight().intersects(tile.getBoundsLeft()) && enemy.getLastSpriteRight()) {
                handleTileLeftCollision(enemy);
                System.out.println("HIT-RIGHT: " + hitcounterRIGHT++);
            }

        }
    }

    public void handleTileTopCollision(GameObject gameObject) {
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

    public void handleTileBottomCollision(GameObject gameObject) {
        //player.setY(tileY + tileHeight);
        gameObject.setVelocityY(1);
    }

    public void handleTileRightCollision(GameObject gameObject) {
        if (gameObject.getVelocityX() != 0) {
            gameObject.setLeftCollision(true);
            gameObject.setVelocityX(0, false);
        }
    }

    public void handleTileLeftCollision(GameObject gameObject) {
        if (gameObject.getVelocityX() != 0) {
            gameObject.setRightCollision(true);
            gameObject.setVelocityX(0, false);
        }
    }

    public void handleEnemyCollision() {
        Iterator<Enemy> iterator = level.getEnemies().iterator();

        if (collisionOn) {
            if (bouncing && player.getCurrentSpriteState() > 5) player.setVelocityX(0, false);

            while (iterator.hasNext()) {
                Enemy e = iterator.next();

                handleEnemyBulletCollision(e, iterator);

                if (e.getBoundsTop().intersects(player.getBoundsBottom())) {
                    handleEnemyTopCollision();
                }


                if (e.getBoundsRight().intersects(player.getBoundsLeft())) {
                    handleEnemyRightCollision();
                    System.out.println("HIT!");
                }

                if (e.getBoundsBottom().intersects(player.getBoundsTop())) {
                    handleEnemyBottomCollision();
                }


                if (e.getBoundsLeft().intersects(player.getBoundsRight())) {
                    handleEnemyLeftCollision();
                    System.out.println("HIT!");
                }
            }
        }
    }

    public void handleEnemyBulletCollision(Enemy enemy, Iterator<Enemy> enemyIterator) {

        for (Bullet bullet : level.getBullets()) {
            //System.out.println(bullet.getX());
            if (bullet.getBoundsTop().intersects(enemy.getBoundsLeft()) || bullet.getBoundsBottom().intersects(enemy.getBoundsLeft())) {
                System.out.println("HIT!");
                level.removeBullet(bullet);
                enemyIterator.remove();
            }
        }
    }


    public void handleEnemyTopCollision() {
        bouncing = true;
        if (player.getCurrentSpriteState() == Player.PLAYER_FALLING_RIGHT || player.getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT
                || player.getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT) {
            player.setVelocityY(-25);
            player.setVelocityX(20, false);
        } else {
            player.setVelocityY(-25);
            player.setVelocityX(-20, false);
        }
        playerHit();
    }


    public void handleEnemyBottomCollision() {
        bouncing = true;
        playerHit();
        if (player.getCurrentSpriteState() == Player.PLAYER_JUMPING_LEFT || player.getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT) {
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

    public void handleEnemyRightCollision() {
        bouncing = true;
        player.setVelocityX(10, false);
        player.setVelocityY(-10);
        playerHit();
    }

    public void handleEnemyLeftCollision() {
        bouncing = true;
        player.setVelocityX(-10, false);
        player.setVelocityY(-10);
        playerHit();
    }

    public void playerHit() {
        soundEffects.HIT.play();
        player.setHp(player.getHp() - 25);
        if (player.getHp() == 0) player.setAlive(false);
    }
}

/*

 */