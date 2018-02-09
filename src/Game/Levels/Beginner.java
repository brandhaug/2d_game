package Game.Levels;

import Game.GameController;
import Game.GameObjects.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Beginner {

    public static final int GROUND_FLOOR_HEIGHT = 128;
    private GraphicsContext gc;
    private Image startingPointImage;
    private Image maceImage;
    private Image sawImage;
    private Image coinImage;
    private long startNanoTime;
    private long currentNanoTime;
    private static List<Tile> tiles;
    public static List<Coin> coins;
    private final int tileCount = 30;
    private int playerX;
    private Enemy mace1;
    private Enemy mace2;
    private Enemy saw1;

    public int [][]coinArray = {
            /*x*/{400,500,600},
            /*y*/{400,350,400}


    };

    public Beginner(GraphicsContext gc) {
        this.gc = gc;
        this.maceImage = new Image("Resources/enemies/Mace.png");
        this.sawImage = new Image("Resources/enemies/Mace.png");
        this.startingPointImage = new Image("/Resources/start.png");
        this.coinImage= new Image("/Resources/coin/CoinAnimation3.png");

        this.tiles = new ArrayList<>();
        addTiles();
        this.coins = new ArrayList<>();
        addCoins();

        mace1 = new Enemy(gc);
        mace2 = new Enemy(gc);
        saw1 = new Enemy(gc);

        draw();
    }

    private void draw() {
        drawStartingPoint();
        drawEnemies();
    }

    private void drawStartingPoint() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;
        gc.drawImage(startingPointImage, 405 - playerX, (int) (150 + 25 * Math.sin(t)));
    }


    private void drawEnemies() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;

        mace1.setX((int) (1100 + (128 * Math.cos(t)) - playerX));
        mace1.setY((int) (300 + 128 * Math.sin(t)));
        gc.drawImage(maceImage, mace1.getX(), mace1.getY());

        mace2.setX((int) (1500 + 128 * Math.sin(t)) - playerX);
        mace2.setY((int) (300 + 128 * Math.cos(t)));
        gc.drawImage(maceImage, mace2.getX(), mace2.getY());

        saw1.setX(2000 - playerX);
        saw1.setY((int) (300 + 128 * Math.sin(t)));
        gc.drawImage(sawImage, saw1.getX(), saw1.getY());
    }

    private void addCoins(){
        if(coinArray[0].length == coinArray[1].length) {
            for (int i = 0; i < coinArray[0].length; i++) {
                coins.add(new Coin(gc, coinArray[0][i], coinArray[1][i]));
            }
        }
    }

    public void drawCoins(){
        for (Coin coin : coins){
            coin.draw(gc,playerX);
        }
    }

    private void addTiles() {
        for (int x = 0; x <= 90 * 64; x += GameController.TILE_SIZE) {
            if (x == 0) {
                for (int y = 0; y < 12 * GameController.TILE_SIZE; y += GameController.TILE_SIZE) {
                    tiles.add(new Tile(gc, TileType.GRASS_LEFT, x, y));
                }
            } else if (x == 40 * 64 - GameController.TILE_SIZE) {
                tiles.add(new Tile(gc, TileType.GRASS_LEFT, x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE));
            } else {
                tiles.add(new Tile(gc, TileType.GRASS_MID, x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE));
            }
        }

        tiles.add(new Tile(gc, TileType.GRASS_MID, 500 + 200 - playerX, 400));
    }

    public void tick(long startNanoTime, long currentNanoTime, int playerX) {
        this.playerX = playerX;
        this.startNanoTime = startNanoTime;
        this.currentNanoTime = currentNanoTime;
        draw();

        for (Tile tile : tiles) {
            tile.tick(playerX);
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }
    public List<Coin> getCoins() {
        return coins;
    }
}
