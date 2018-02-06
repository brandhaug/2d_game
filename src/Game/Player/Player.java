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
    private BufferedImage spriteSheet;
    private BufferedImage[] sprites;

    private final int spriteSheetWidth = 85;
    private final int spriteSheetHeight = 74;
    private final int spriteSheetCols = 11;
    private int currentColumnIndex = 0;

    public Player(GraphicsContext gc) {
        try {
            spriteSheet = ImageIO.read(new File(getClass().getResource("/Resources/player/blink3.png").getPath()));
            sprites = new BufferedImage[spriteSheetCols];

            for (int i = 0; i < spriteSheetCols; i++) {
                sprites[i] = spriteSheet.getSubimage(i * spriteSheetWidth, 0, spriteSheetWidth, spriteSheetHeight);
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
        gc.fillRect(x, y + spriteSheetHeight - 5, spriteSheetWidth, 5);

        //Top
        gc.fillRect(x, y, spriteSheetWidth, 5);

        gc.setFill(Color.GREEN);

        //Right
        gc.fillRect(x + spriteSheetWidth - 5, y, 5, spriteSheetHeight);

        //Left
        gc.fillRect(x, y, 5, spriteSheetHeight);

        Image sprite = SwingFXUtils.toFXImage(sprites[getNextColumn()], null);
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

    public void tick(GraphicsContext gc) {
        if (velocityX >= MAX_VELOCITY_X) {
            x += MAX_VELOCITY_X;
        } else if (velocityX <= MAX_VELOCITY_X * -1) {
            x -= MAX_VELOCITY_X;
        } else {
            x += velocityX;
        }

        
        y += velocityY;

        if (y > GameController.HEIGHT - 190) {
            velocityY = 0;
        } else {
            velocityY += 5;
        }


        draw(gc);

    }
}
