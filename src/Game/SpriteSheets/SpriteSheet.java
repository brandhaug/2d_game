package Game.SpriteSheets;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheet {
    private String pathName;
    private int cols;
    private int spriteWidth;
    private int spriteHeight;

    private Image[] sprites;

    private int currentColumnIndex = 0;

    public SpriteSheet(String pathName, int cols, int spriteWidth, int spriteHeight) {
        this.pathName = pathName;
        this.cols = cols;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        try {
            initializeSprites();
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

    private void initializeSprites() throws IOException {
        BufferedImage spriteSheet = ImageIO.read(new File(getClass().getResource(pathName).getPath()));
        sprites = new Image[cols];
        for (int i = 0; i < cols; i++) {
            sprites[i] = SwingFXUtils.toFXImage(spriteSheet.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight), null);
        }
    }

    public void draw(GraphicsContext gc, int x, int y, int currentSpriteState, int lastSpriteState) {
        gc.drawImage(sprites[getNextColumnIndex(currentSpriteState, lastSpriteState)], x, y);
        drawBounds(gc, x, y);
    }

    private void drawBounds(GraphicsContext gc, int x, int y) {
        gc.setStroke(Color.BLUE);
        gc.strokeRect(x + 10, y + spriteHeight / 2, spriteWidth - 20, spriteHeight / 2); //Bottom

        gc.setStroke(Color.RED);
        gc.strokeRect(x + 10, y, spriteWidth - 20, spriteHeight / 2); //Top

        gc.setStroke(Color.GREEN);
        gc.strokeRect(x + spriteWidth - 5, y + 5, 5, spriteHeight - 10); //Right

        gc.setStroke(Color.YELLOW);
        gc.strokeRect(x, y + 5, 5, spriteHeight - 10); //Left
    }

    //TODO: Make method more effective
    private int getNextColumnIndex(int currentSpriteState, int lastSpriteState) {
        if (currentSpriteState != lastSpriteState || currentColumnIndex == cols - 1 || cols == 1) {
            currentColumnIndex = 0;
        }

        return currentColumnIndex++;
    }
}
