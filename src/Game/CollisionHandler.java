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

            if (player.getBoundsTop().intersects(tile.getBoundsBottom()) && player.getCurrentSpriteState() != Player.PLAYER_FALLING_LEFT && player.getCurrentSpriteState() != Player.PLAYER_FALLING_RIGHT) {
                handleTileTopCollision(tile.getY(), tile.getSize());
            }

            if ((player.getBoundsRight().intersects(tile.getBoundsLeft()) && player.getCurrentSpriteState() == Player.PLAYER_RUNNING_RIGHT) ||
                    (player.getBoundsLeft().intersects(tile.getBoundsRight()) && player.getCurrentSpriteState() == Player.PLAYER_RUNNING_LEFT)) {
                handleTileSideCollision();
            }
        }
    }

    public void handleTileBottomCollision(int tileY) {
        player.setY(tileY - player.getCurrentSpriteSheet().getSpriteHeight() + 10);
        player.setVelocityY(0);
    }

    public void handleTileTopCollision(int tileY, int tileHeight) {
        player.setY(tileY + tileHeight);
        player.setVelocityY(1);
    }

    public void handleTileSideCollision() {
        player.setVelocityX(0);
    }
}
