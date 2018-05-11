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
    private int cols;
    private int spriteWidth;
    private int spriteHeight;

    private Image[] sprites;

    private int currentColumnIndex = 0;

    public SpriteSheet(String pathName, int cols, int spriteWidth, int spriteHeight) {
        this.cols = cols;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        try {
            initializeSprites(pathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    private void initializeSprites(String pathName) throws IOException {
        BufferedImage spriteSheet = ImageIO.read(new File(getClass().getResource(pathName).getPath()));
        sprites = new Image[cols];
        for (int i = 0; i < cols; i++) {
            sprites[i] = SwingFXUtils.toFXImage(spriteSheet.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight), null);
        }
    }

    public void render(GraphicsContext gc, int x, int y, int currentSpriteState, int lastSpriteState) {
        gc.drawImage(sprites[getNextColumnIndex(currentSpriteState, lastSpriteState)], x, y);
    }

    private int getNextColumnIndex(int currentSpriteState, int lastSpriteState) {
        if (currentSpriteState != lastSpriteState || currentColumnIndex == cols - 1) {
            currentColumnIndex = 0;
            return currentColumnIndex;
        }

        return currentColumnIndex++;
    }
}
