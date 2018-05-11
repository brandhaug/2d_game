package Game.SpriteSheets;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheet {
    private int cols;
    private int spriteWidth;
    private int spriteHeight;
    private int currentColumnIndex = 0;
    private Image[] sprites;

    /**
     * Initializes variables and sprites given by path name
     *
     * @param pathName     the name of the path
     * @param cols         the amount of columns
     * @param spriteWidth  the sprite width
     * @param spriteHeight the sprite height
     */
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

    /**
     * Gets the sprite width
     *
     * @return spriteWidth
     */
    public int getSpriteWidth() {
        return spriteWidth;
    }

    /**
     * Gets the sprite height
     *
     * @return spriteHeight
     */
    public int getSpriteHeight() {
        return spriteHeight;
    }

    /**
     * Initializes the sprites
     *
     * @param pathName the path name
     * @throws IOException
     */
    private void initializeSprites(String pathName) throws IOException {
        BufferedImage spriteSheet = ImageIO.read(new File(getClass().getResource(pathName).getPath()));
        sprites = new Image[cols];
        for (int i = 0; i < cols; i++) {
            sprites[i] = SwingFXUtils.toFXImage(spriteSheet.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight), null);
        }
    }

    /**
     * Renders the GraphicsContext according to variables passed in
     *
     * @param gc                 the Graphics Context
     * @param x                  the x position
     * @param y                  the y position
     * @param currentSpriteState the current sprite state
     * @param lastSpriteState    the last sprite state
     * @see GraphicsContext
     */
    public void render(GraphicsContext gc, int x, int y, int currentSpriteState, int lastSpriteState) {
        gc.drawImage(sprites[getNextColumnIndex(currentSpriteState, lastSpriteState)], x, y);
    }

    /**
     * Gets the next column index
     *
     * @param currentSpriteState the current sprite state
     * @param lastSpriteState    the last sprite state
     * @return the next column index
     */
    private int getNextColumnIndex(int currentSpriteState, int lastSpriteState) {
        if (currentSpriteState != lastSpriteState || currentColumnIndex == cols - 1) {
            currentColumnIndex = 0;
            return currentColumnIndex;
        }

        return currentColumnIndex++;
    }
}
