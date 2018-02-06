package Game.Player;

import Game.GameController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {

    private int x;
    private int y;
    private int velocityX = 0;
    private int velocityY = 10;
    private final int MAX_VELOCITY_X = 15;


    private BufferedImage idleSpriteSheet;
    private BufferedImage[] idleSprites;
    private final int idleSpriteSheetWidth = 144;
    private final int idleSpriteSheetHeight = 152;
    private final int idleSpriteSheetCols = 12;

    private BufferedImage runSpriteSheet;
    private BufferedImage[] runSprites;
    private final int runSpriteSheetWidth = 198;
    private final int runSpriteSheetHeight = 152;
    private final int runSpriteSheetCols = 18;

//    private BufferedImage jumpSpriteSheet;
//    private BufferedImage[] jumpSprites;
//    private final int jumpSpriteSheetWidth = 198;
//    private final int jumpSpriteSheetHeight = 152;
//    private final int jumpSpriteSheetCols = 18;



    private int currentColumnIndex = 0;

    public Player(GraphicsContext gc) {
        try {
            idleSpriteSheet = ImageIO.read(new File(getClass().getResource("/Resources/player/idle.png").getPath()));
            idleSprites = new BufferedImage[idleSpriteSheetCols];

            for (int i = 0; i < idleSpriteSheetCols; i++) {
                idleSprites[i] = idleSpriteSheet.getSubimage(i * idleSpriteSheetWidth, 0, idleSpriteSheetWidth, idleSpriteSheetHeight);
            }

            runSpriteSheet = ImageIO.read(new File(getClass().getResource("/Resources/player/run.png").getPath()));
            runSprites = new BufferedImage[runSpriteSheetCols];

            for (int i = 0; i < runSpriteSheetCols; i++) {
                runSprites[i] = runSpriteSheet.getSubimage(i * runSpriteSheetWidth, 0, runSpriteSheetWidth, runSpriteSheetHeight);
            }

            tick(gc);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);

        //Bottom
        gc.fillRect(x, y + runSpriteSheetHeight - 5, runSpriteSheetWidth, 5);

        //Top
        gc.fillRect(x, y, runSpriteSheetWidth, 5);

        gc.setFill(Color.GREEN);

        //Right
        gc.fillRect(x + runSpriteSheetWidth - 5, y, 5, runSpriteSheetHeight);

        //Left
        gc.fillRect(x, y, 5, runSpriteSheetHeight);

        Image sprite = SwingFXUtils.toFXImage(runSprites[getNextColumn()], null);
        gc.drawImage(sprite, x, y);
    }

    public int getNextColumn() {
        if (currentColumnIndex == runSprites.length - 1) {
            currentColumnIndex = 0;
        } else {
            currentColumnIndex++;
        }

        return currentColumnIndex;
    }

    public void tick(GraphicsContext gc) {
        if (velocityX >= MAX_VELOCITY_X) {
            x += MAX_VELOCITY_X;
        } else if (velocityX <= MAX_VELOCITY_X * -1) {
            x -= MAX_VELOCITY_X;
        } else {
            x += velocityX;
        }

        
        y += velocityY;

        if (y > GameController.HEIGHT - runSpriteSheetHeight - 100) {
            velocityY = 0;
        } else {
            velocityY += 5;
        }


        draw(gc);

    }
}
