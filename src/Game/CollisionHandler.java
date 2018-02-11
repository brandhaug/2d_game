package Game;

import Game.GameObjects.Player;
import Game.GameObjects.Tile;
import Game.Levels.Beginner;

public class CollisionHandler {
    private Player player;
    private Beginner level;

    public CollisionHandler(Player player, Beginner level) {
        this.player = player;
        this.level = level;
    }

    public void tick() {
        handleTileCollision();
    }

    public void handleTileCollision() {
        player.setVelocityY(player.getVelocityY() + 2);

        for (Tile tile : level.getTiles()) {
            if (player.getBoundsBottom().intersects(tile.getBoundsTop()) && player.getCurrentSpriteState() != Player.PLAYER_JUMPING_RIGHT && player.getCurrentSpriteState() != Player.PLAYER_JUMPING_LEFT) {
                handleTileBottomCollision(tile.getY());
            }

            if (player.getBoundsTop().intersects(tile.getBoundsBottom())) {
                handleTileTopCollision();
            }

            if ((player.getBoundsLeft().intersects(tile.getBoundsRight())) || (player.getBoundsRight().intersects(tile.getBoundsLeft()))) {
                handleTileSideCollision();
            }
        }
    }

    public void handleTileBottomCollision(int tileY) {
        player.setY(tileY - player.getCurrentSpriteSheet().getSpriteHeight() + 10);
        player.setVelocityY(0);
    }

    public void handleTileTopCollision() {
        player.setVelocityY(0);
    }

    public void handleTileSideCollision() {
        player.setVelocityX(0);
    }
}
