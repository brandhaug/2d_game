package Game.GameObjects;


import Game.GameObjects.Player;
import Game.Levels.Level;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class PlayerTest {
    @Test
    public void testStates() {
        Player player = new Player(200, 200, "A");

        player.setVelocityX(0, false);
        player.setVelocityY(0);
        player.handleSpriteStates();
        assertEquals(player.getCurrentSpriteState(), Player.PLAYER_IDLING_RIGHT);
        assertEquals(player.isIdling(), true);
        assertEquals(player.isJumping(), false);
        assertEquals(player.isRunning(), false);
        assertEquals(player.isFalling(), false);


        player.setVelocityX(12, false);
        player.handleSpriteStates();
        assertEquals(player.getCurrentSpriteState(), Player.PLAYER_RUNNING_RIGHT);
        assertEquals(player.isIdling(), false);
        assertEquals(player.isJumping(), false);
        assertEquals(player.isRunning(), true);
        assertEquals(player.isFalling(), false);


        player.setVelocityX(-12, false);
        player.handleSpriteStates();
        assertEquals(player.getCurrentSpriteState(), Player.PLAYER_RUNNING_LEFT);
        assertEquals(player.isIdling(), false);
        assertEquals(player.isJumping(), false);
        assertEquals(player.isRunning(), true);
        assertEquals(player.isFalling(), false);


        player.setVelocityX(0, false);
        player.handleSpriteStates();
        assertEquals(player.getCurrentSpriteState(), Player.PLAYER_IDLING_LEFT);
        assertEquals(player.isIdling(), true);
        assertEquals(player.isJumping(), false);
        assertEquals(player.isFalling(), false);


        player.setVelocityY(-10);
        player.handleSpriteStates();
        assertEquals(player.getCurrentSpriteState(), Player.PLAYER_JUMPING_LEFT);
        assertEquals(player.isIdling(), false);
        assertEquals(player.isJumping(), true);
        assertEquals(player.isRunning(), false);
        assertEquals(player.isFalling(), false);

        player.setVelocityX(0, false);
        player.setVelocityY(10);
        player.handleSpriteStates();
        assertEquals(player.getCurrentSpriteState(), Player.PLAYER_FALLING_LEFT);
        assertEquals(player.isIdling(), false);
        assertEquals(player.isJumping(), false);
        assertEquals(player.isRunning(), false);
        assertEquals(player.isFalling(), true);

        player.setVelocityX(5, false);
        player.setVelocityY(10);
        player.handleSpriteStates();
        assertEquals(player.getCurrentSpriteState(), Player.PLAYER_FALLING_RIGHT);
        assertEquals(player.isIdling(), false);
        assertEquals(player.isJumping(), false);
        assertEquals(player.isRunning(), false);
        assertEquals(player.isFalling(), true);
    }
}
