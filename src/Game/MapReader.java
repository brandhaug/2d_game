package Game;

import Game.GameObjects.Coin;
import Game.GameObjects.Tile;
import Game.GameObjects.TileType;
import Game.Levels.Level;

import java.io.*;
import java.util.Scanner;

public class MapReader {
    private final int MAP_HEIGHT = 6;
    private final int MAP_WIDTH = 100;

    private final char TILE = '-';
    private final char COIN = 'c';
    private final char ENEMY = 'e';
    private final char PLAYER = 'p';

    // TODO: Lag egne Exceptions
    public char[][] loadMap(String fileName) throws FileNotFoundException {
        File file = new File(getClass().getResource("/Resources/maps/" + fileName).getPath());
        Scanner scanner = new Scanner(file);

        int index = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.replaceAll("\\s", "");
            char[] lineArr = line.toCharArray();

            if (index == 0) {
                char[][] map = new char[][MAP_WIDTH];
            } else {
                char[] lineArr = line.toCharArray();
                map[index] = lineArr;
            }



            index++;
        }

        return map;
    }
}
