package Game.SpriteSheets;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheet {
    private String pathname;
    private int cols;
    private int spriteWidth;
    private int spriteHeight;

    private BufferedImage spriteSheet;
    private BufferedImage[] sprites;

    private int currentColumnIndex = 0;

    public SpriteSheet(String pathname, int cols, int spriteWidth, int spriteHeight) {
        this.pathname = pathname;
        this.cols = cols;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        try {
            initializeSprites();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeSprites() throws IOException {
        spriteSheet = ImageIO.read(new File(getClass().getResource(pathname).getPath()));
        sprites = new BufferedImage[cols];
        for (int i = 0; i < cols; i++) {
            sprites[i] = spriteSheet.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight);
        }
    }
    
    public void draw(GraphicsContext gc, int x, int y, int currentSpriteState, int lastSpriteState) {
        gc.setFill(Color.BLUE);

        //Bottom
        gc.fillRect(x, y + spriteHeight, spriteWidth, 5);

        //Top
        gc.fillRect(x, y, spriteWidth, 5);

        gc.setFill(Color.GREEN);

        //Right
        gc.fillRect(x + spriteWidth - 5, y, 5, spriteHeight);

        //Left
        gc.fillRect(x, y, 5, spriteHeight);

        Image sprite = SwingFXUtils.toFXImage(sprites[getNextColumn(currentSpriteState, lastSpriteState)], null);
        gc.drawImage(sprite, x, y);
    }

    private int getNextColumn(int currentSpriteState, int lastSpriteState) {
        if (currentSpriteState != lastSpriteState || currentColumnIndex == cols - 1) {
            currentColumnIndex = 0;
        }

        return currentColumnIndex++;
    }
}
