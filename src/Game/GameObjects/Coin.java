package Game.GameObjects;

import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

public class Coin extends GameObject {
    private int x;
    private int y;
    private int velocityX;
    private int velocityY;

    private SpriteSheet coinSpriteSheet;
    private GraphicsContext gc;


    private final String coinRotatePath = "/Resources/coin/CoinAnimation3.png";
    private final int coinSpriteWidth = 64;
    private final int coinSpriteHeight = 64;
    private final int coinSpriteSheetCols = 16;

    //private int currentSpriteState;
    //private int lastSpriteState;


    public Coin(GraphicsContext gc, int x, int y) {
        initializeSpriteSheets();
        this.gc = gc;
        this.x = x;
        this.y = y;
    }

    public Coin(GraphicsContext gc) {
        initializeSpriteSheets();
        this.gc = gc;
        this.x = x;
        this.y = y;
    }

    public void draw(GraphicsContext gc, int playerX) {
        coinSpriteSheet.draw(gc, x - playerX, y,0,0);
    }

    /*
    public void draw(GraphicsContext gc, int c[][]) {
        for(int i=0;i<c.length;i++){
                coinSpriteSheet.draw(gc, c[0][i], c[1][i],0,0);
            }
    }
    */

    public void delete(int ca[][]){

    }




        @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    @Override
    public int getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    @Override
    public int getVelocityY() {
        return velocityY;
    }

    private void initializeSpriteSheets() {
        coinSpriteSheet = new SpriteSheet(coinRotatePath, coinSpriteSheetCols, coinSpriteWidth, coinSpriteHeight);
    }
}
