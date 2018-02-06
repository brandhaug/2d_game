package Game.Player;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {

    private int x;
    private int y;
    private int velocityX = 2;
    private int velocityY = -2;
    private BufferedImage spriteSheet;
    private BufferedImage[] sprites;

    private final int spriteSheetWidth = 85;
    private final int spriteSheetHeight = 74;
    private final int spriteSheetCols = 11;
    private int currentColumnIndex = 0;

    public Player(GraphicsContext gc) {
        try {
            spriteSheet = ImageIO.read(new File("C:\\Users\\brandhaug-laptop\\IdeaProjects\\2d_game\\res\\images\\player\\blink3.png"));
            sprites = new BufferedImage[spriteSheetCols];

            for (int i = 0; i < spriteSheetCols; i++) {
                sprites[i] = spriteSheet.getSubimage(i * spriteSheetWidth, 0, spriteSheetWidth, spriteSheetHeight);
            }

            draw(gc, currentColumnIndex, spriteSheetWidth, spriteSheetHeight);

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

    public void draw(GraphicsContext gc, int currentColumnIndex, int x, int y) {
        Image sprite = SwingFXUtils.toFXImage(sprites[currentColumnIndex], null);
        gc.drawImage(sprite, x, y);
    }

    public int getNextColumn() {
        if (currentColumnIndex == sprites.length - 1) {
            currentColumnIndex = 0;
        } else {
            currentColumnIndex++;
        }

        return currentColumnIndex;
    }
}
