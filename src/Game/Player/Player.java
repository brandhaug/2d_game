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

    final int spriteSheetWidth = 1024;
    final int spriteSheetHeight = 256;
    final int spriteSheetRows = 1;
    final int spriteSheetCols = 11;

    public Player(GraphicsContext gc) {
        try {
            spriteSheet = ImageIO.read(new File("file:res/images/player/blink.png"));
            sprites = new BufferedImage[spriteSheetRows * spriteSheetCols];

            for (int i = 0; i < spriteSheetRows; i++) {
                for (int j = 0; j < spriteSheetCols; j++) {
                    sprites[(i * spriteSheetCols) + j] = spriteSheet.getSubimage(
                            j * spriteSheetWidth,
                            i * spriteSheetHeight,
                            spriteSheetWidth,
                            spriteSheetHeight
                    );

                    draw(gc, sprites[(i * spriteSheetCols) + j], i * 64, j * 64);
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
// The above line thspriteSheetRows an checked IOException which must be caught.


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

    public void draw(GraphicsContext gc, BufferedImage sprite, int x, int y) {
        gc.drawImage(SwingFXUtils.toFXImage(sprite, null), x, y);
    }
}
