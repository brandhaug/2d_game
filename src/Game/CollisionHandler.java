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

    public void handleCollision() {
        player.setVelocityY(player.getVelocityY() + 2);

        for (Tile tile : level.getTiles()) {
            if (player.getBoundsBottom().intersects(tile.getBoundsTop()) && player.getCurrentSpriteState() != Player.PLAYER_JUMPING) {
                handleBottomCollision(tile.getY());
            }

            if (player.getBoundsTop().intersects(tile.getBoundsBottom())) {
                handleTopCollision();
            }

            if ((player.getBoundsLeft().intersects(tile.getBoundsRight()) && player.getCurrentSpriteState() == Player.PLAYER_RUNNING_LEFT) ||
                    (player.getBoundsRight().intersects(tile.getBoundsLeft()) && player.getCurrentSpriteState() == Player.PLAYER_RUNNING_RIGHT)) {
                handleSideCollision();
            }
        }
    }

    public void handleBottomCollision(int tileY) {
        player.setY(tileY - player.getCurrentSpriteSheet().getSpriteHeight() + 10);
        player.setVelocityY(0);
    }

    public void handleTopCollision() {
        player.setVelocityY(0);
    }

    public void handleSideCollision() {
        player.setVelocityX(0);
    }
}
