package Game;

import Game.GameObjects.Coin;
import Game.GameObjects.Enemy;
import Game.GameObjects.Player;
import Game.GameObjects.Tile;
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


    public CollisionHandler(Player player, Level level,SoundEffects soundEffects) {
        this.player = player;
        this.level = level;
        this.soundEffects = soundEffects;

    }

    public void tick() {
        handleTileCollision();
        handleCoinCollision();
        handleEnemyCollision();
    }

    public void handleCoinCollision(){
        Iterator<Coin> iterator = level.getCoins().iterator();

        while (iterator.hasNext()) {
            Coin c = iterator.next();
            if (c.getBoundsBottom().intersects(player.getBoundsTop()) || c.getBoundsBottom().intersects(player.getBoundsRight())
                    || c.getBoundsBottom().intersects(player.getBoundsBottom()) || c.getBoundsBottom().intersects(player.getBoundsLeft())) {
                iterator.remove();
                level.addCoinCounter();
                soundEffects.COIN.play();
            }
        }
    }

    public void handleTileCollision() {
        player.setLeftCollision(false);
        player.setRightCollision(false);

        player.setVelocityY(player.getVelocityY() + 2);

        for (Tile tile : level.getTiles()) {
            if (player.getBoundsBottom().intersects(tile.getBoundsTop()) && player.getCurrentSpriteState() != Player.PLAYER_JUMPING_RIGHT && player.getCurrentSpriteState() != Player.PLAYER_JUMPING_LEFT) {
                handleTileTopCollision();
            }

            if (player.getBoundsTop().intersects(tile.getBoundsBottom()) && player.getCurrentSpriteState() != Player.PLAYER_FALLING_LEFT && player.getCurrentSpriteState() != Player.PLAYER_FALLING_RIGHT) {
                handleTileBottomCollision();
            }

            if (player.getBoundsLeft().intersects(tile.getBoundsRight()) && !player.getLastSpriteRight()) {
                handleTileRightCollision();
            }

            if (player.getBoundsRight().intersects(tile.getBoundsLeft()) && player.getLastSpriteRight()) {
                handleTileLeftCollision();
            }
        }
    }

    public void handleTileTopCollision() {
        //player.setY(tileY - player.getCurrentSpriteSheet().getSpriteHeight() + 10);
        player.setVelocityY(0);
        bouncing = false;
    }

    public void handleTileBottomCollision() {
        //player.setY(tileY + tileHeight);
        player.setVelocityY(1);
    }

    public void handleTileRightCollision() {
        if (player.getVelocityX() != 0) {
            player.setLeftCollision(true);
            player.setVelocityX(0);
        }
    }

    public void handleTileLeftCollision() {
        if (player.getVelocityX() != 0) {
            player.setRightCollision(true);
            player.setVelocityX(0);
        }
    }

    public void handleEnemyCollision() {
        if (collisionOn == true){
        if (bouncing && player.getCurrentSpriteState() > 5) player.setVelocityX(0);

        for (Enemy enemy : level.getEnemies()) {
            if (enemy.getBoundsTop().intersects(player.getBoundsBottom())) {
                handleEnemyTopCollision();
                System.out.println(player.getHp());
            }

            if (enemy.getBoundsRight().intersects(player.getBoundsLeft())) {
                handleEnemyRightCollision();
                System.out.println(player.getHp());
            }

            if (enemy.getBoundsBottom().intersects(player.getBoundsTop())) {
                handleEnemyBottomCollision();
                System.out.println(player.getHp());
            }

            if (enemy.getBoundsLeft().intersects(player.getBoundsRight())) {
                handleEnemyLeftCollision();
                System.out.println(player.getHp());
            }
        }
    }
    }


    public void handleEnemyTopCollision() {
        bouncing = true;
        if(player.getCurrentSpriteState() == Player.PLAYER_FALLING_RIGHT || player.getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT
                || player.getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT) {
            player.setVelocityY(-25);
            player.setVelocityX(30);
        }else{
            player.setVelocityY(-25);
            player.setVelocityX(-30);
        }
        playerHit();
    }


    public void handleEnemyBottomCollision() {
        bouncing = true;
        playerHit();
        if(player.getCurrentSpriteState() == Player.PLAYER_JUMPING_LEFT || player.getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT) {
            player.setVelocityY(20);
        }

        if(player.getCurrentSpriteState() == Player.PLAYER_IDLING_LEFT) {
            collisionOn = false;
            player.setVelocityY(-20);
        }


        if(player.getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT) {
            collisionOn = false;
            player.setVelocityY(-20);
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Hit");
                collisionOn = true;
                timer.cancel();
            }
        }, 1000);
    }

    public void handleEnemyRightCollision() {
        bouncing = true;
        player.setVelocityX(10);
        playerHit();
    }

    public void handleEnemyLeftCollision() {
        bouncing = true;
        player.setVelocityX(-10);
        playerHit();
    }

    public void playerHit(){
        player.setHp(player.getHp()-25);
        if(player.getHp() >= 25)
        soundEffects.HIT.play();
    }

}

/*

*/