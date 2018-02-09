package Game.Levels;

import Game.GameObjects.Coin;
import Game.GameObjects.Enemy;
import Game.GameObjects.Tile;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {
    private List<Tile> tiles;
    private List<Coin> coins;
    private List<Enemy> enemies;

    public Level() {
        tiles = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();

    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

}
