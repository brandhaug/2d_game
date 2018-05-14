package Game.Levels;


import Game.GameObjects.TileType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class LevelsTest {
    @Test
    public void testTileType() {
        char[][] map = new char[][]{
                {'-', '-', '-', '0', '-', '0', '-', '0', '0'},
                {'-', '-', '-', '0', '-', '0', '0', '-', '0'},
                {'-', '-', '-', '0', '-', '0', '0', '0', '-'}
        };

        Level level = new Level("classic/standard/1_Beginner");

        assertEquals(TileType.GRASS_LEFT, level.getTileType(map, 0, 0));
        assertEquals(TileType.DIRT_LEFT, level.getTileType(map, 1, 0));
        assertEquals(TileType.DIRT_LEFT_CORNER, level.getTileType(map, 2, 0));

        assertEquals(TileType.GRASS_MID, level.getTileType(map, 0, 1));
        assertEquals(TileType.DIRT, level.getTileType(map, 1, 1));
        assertEquals(TileType.DIRT_BOTTOM, level.getTileType(map, 2, 1));

        assertEquals(TileType.GRASS_RIGHT, level.getTileType(map, 0, 2));
        assertEquals(TileType.DIRT_RIGHT, level.getTileType(map, 1, 2));
        assertEquals(TileType.DIRT_RIGHT_CORNER, level.getTileType(map, 2, 2));

        assertEquals(TileType.GRASS_TOP, level.getTileType(map, 0, 4));
        assertEquals(TileType.DIRT_COLUMN, level.getTileType(map, 1, 4));
        assertEquals(TileType.DIRT_COLUMN, level.getTileType(map, 2, 4));

        assertEquals(TileType.GRASS_TOP, level.getTileType(map, 0, 6));
        assertEquals(TileType.GRASS_TOP, level.getTileType(map, 1, 7));
        assertEquals(TileType.GRASS_TOP, level.getTileType(map, 2, 8));
    }
}
