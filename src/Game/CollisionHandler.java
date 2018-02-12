package Game;

import Game.GameObjects.Coin;
import Game.GameObjects.Player;
import Game.GameObjects.Tile;
import Game.Levels.Level;

import java.util.Iterator;

public class CollisionHandler {
    private Player player;
    private Level level;

    public CollisionHandler(Player player, Level level) {
        this.player = player;
        this.level = level;
    }

    public void tick() {
        handleTileCollision();
        handleCoinCollision();
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

            if (player.getBoundsLeft().intersects(tile.getBoundsRight()) && player.getCurrentSpriteState() == Player.PLAYER_RUNNING_LEFT) {
                handleTileRightCollision();
            }

            if (player.getBoundsRight().intersects(tile.getBoundsLeft()) && player.getCurrentSpriteState() == Player.PLAYER_RUNNING_RIGHT) {
                handleTileLeftCollision();
            }
        }
    }


    public void handleCoinCollision() {
        Iterator<Coin> iterator = level.getCoins().iterator();
        while (iterator.hasNext()) {
            Coin c = iterator.next();
            if (c.getBoundsBottom().intersects(player.getBoundsTop()) || c.getBoundsBottom().intersects(player.getBoundsRight())
                    || c.getBoundsBottom().intersects(player.getBoundsBottom()) || c.getBoundsBottom().intersects(player.getBoundsLeft())) {
                iterator.remove();
                level.addCoinCounter();
            }
        }
    }

    public void handleCoinBottomCollision(int coinY) {
        player.setY(coinY - player.getCurrentSpriteSheet().getSpriteHeight() - 10);
        player.setVelocityY(0);
    }

    public void handleTileTopCollision() {
        //player.setY(tileY - player.getCurrentSpriteSheet().getSpriteHeight() + 10);
        player.setVelocityY(0);
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
}
