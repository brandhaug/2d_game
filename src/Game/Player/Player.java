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

    private int PLAYER_IDLING = 0;
    private int PLAYER_RUNNING_RIGHT = 1;
    private int PLAYER_RUNNING_LEFT = 2;
    private int PLAYER_JUMPING = 3;
    private int currentSpriteState = 0;
    private int lastSpriteState = 0;

    private BufferedImage idleSpriteSheet;
    private BufferedImage[] idleSprites;
    private final int idleSpriteSheetWidth = 144;
    private final int idleSpriteSheetHeight = 152;
    private final int idleSpriteSheetCols = 12;

    private BufferedImage rightRunSpriteSheet;
    private BufferedImage[] rightRunSprites;
    private final int rightRunSpriteSheetWidth = 198;
    private final int rightRunSpriteSheetHeight = 152;
    private final int rightRunSpriteSheetCols = 18;

    private BufferedImage jumpSpriteSheet;
    private BufferedImage[] jumpSprites;
    private final int jumpSpriteSheetWidth = 167;
    private final int jumpSpriteSheetHeight = 155;
    private final int jumpSpriteSheetCols = 2;


    private int currentColumnIndex = 0;

    public Player(GraphicsContext gc) {
        try {
            initializeSprites();
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

        if (currentSpriteState == PLAYER_IDLING) {
            //Bottom
            gc.fillRect(x, y + idleSpriteSheetHeight, idleSpriteSheetWidth, 5);

            //Top
            gc.fillRect(x, y, idleSpriteSheetWidth, 5);

            gc.setFill(Color.GREEN);

            //Right
            gc.fillRect(x + idleSpriteSheetWidth - 5, y, 5, idleSpriteSheetHeight);

            //Left
            gc.fillRect(x, y, 5, idleSpriteSheetHeight);

            Image sprite = SwingFXUtils.toFXImage(idleSprites[getNextColumn()], null);
            gc.drawImage(sprite, x, y);
        } else if (currentSpriteState == PLAYER_RUNNING_RIGHT || currentSpriteState == PLAYER_RUNNING_LEFT) {
            //TODO: Implement for left running

            //Bottom
            gc.fillRect(x, y + rightRunSpriteSheetHeight - 5, rightRunSpriteSheetWidth, 5);

            //Top
            gc.fillRect(x, y, rightRunSpriteSheetWidth, 5);

            gc.setFill(Color.GREEN);

            //Right
            gc.fillRect(x + rightRunSpriteSheetWidth - 5, y, 5, rightRunSpriteSheetHeight);

            //Left
            gc.fillRect(x, y, 5, rightRunSpriteSheetHeight);

            Image sprite = SwingFXUtils.toFXImage(rightRunSprites[getNextColumn()], null);
            gc.drawImage(sprite, x, y);
        } else if (currentSpriteState == PLAYER_JUMPING) {
            //Bottom
            gc.fillRect(x, y + jumpSpriteSheetHeight - 5, jumpSpriteSheetWidth, 5);

            //Top
            gc.fillRect(x, y, jumpSpriteSheetWidth, 5);

            gc.setFill(Color.GREEN);

            //Right
            gc.fillRect(x + jumpSpriteSheetWidth - 5, y, 5, jumpSpriteSheetHeight);

            //Left
            gc.fillRect(x, y, 5, jumpSpriteSheetHeight);

            Image sprite = SwingFXUtils.toFXImage(jumpSprites[getNextColumn()], null);
            gc.drawImage(sprite, x, y);
        }
    }

    public int getNextColumn() {
        if (currentSpriteState != lastSpriteState) {
            currentColumnIndex = 0;
        }
        //TODO: Implement for left running
        if (currentSpriteState == PLAYER_RUNNING_RIGHT && currentColumnIndex == rightRunSprites.length - 1) {
            return 0;
        } else if (currentSpriteState == PLAYER_RUNNING_LEFT && currentColumnIndex == rightRunSprites.length - 1) {
            return 0;
        } else if (currentSpriteState == PLAYER_IDLING && currentColumnIndex == idleSprites.length - 1) {
            return 0;
        } else if (currentSpriteState == PLAYER_JUMPING && currentColumnIndex == jumpSprites.length - 1) {
            return 0;
        }

        return currentColumnIndex++;
    }

    public void tick(GraphicsContext gc) {
        lastSpriteState = currentSpriteState;

        if (velocityX >= MAX_VELOCITY_X) {
            x += MAX_VELOCITY_X;
        } else if (velocityX <= MAX_VELOCITY_X * -1) {
            x -= MAX_VELOCITY_X;
        } else {
            x += velocityX;
        }

        if (velocityY != 0) {
            currentSpriteState = PLAYER_JUMPING;
        } else if (velocityX > 0) {
            currentSpriteState = PLAYER_RUNNING_RIGHT;
        } else if (velocityX < 0) {
            currentSpriteState = PLAYER_RUNNING_LEFT;
        } else {
            currentSpriteState = PLAYER_IDLING;
        }


        y += velocityY;

        if (y > GameController.HEIGHT - rightRunSpriteSheetHeight - 100) {
            velocityY = 0;
        } else {
            velocityY += 5;
        }


        draw(gc);
    }

    private void initializeSprites() throws IOException {
        idleSpriteSheet = ImageIO.read(new File(getClass().getResource("/Resources/player/idle.png").getPath()));
        idleSprites = new BufferedImage[idleSpriteSheetCols];
        for (int i = 0; i < idleSpriteSheetCols; i++) {
            idleSprites[i] = idleSpriteSheet.getSubimage(i * idleSpriteSheetWidth, 0, idleSpriteSheetWidth, idleSpriteSheetHeight);
        }

        rightRunSpriteSheet = ImageIO.read(new File(getClass().getResource("/Resources/player/run.png").getPath()));
        rightRunSprites = new BufferedImage[rightRunSpriteSheetCols];
        for (int i = 0; i < rightRunSpriteSheetCols; i++) {
            rightRunSprites[i] = rightRunSpriteSheet.getSubimage(i * rightRunSpriteSheetWidth, 0, rightRunSpriteSheetWidth, rightRunSpriteSheetHeight);
        }

        jumpSpriteSheet = ImageIO.read(new File(getClass().getResource("/Resources/player/jump.png").getPath()));
        jumpSprites = new BufferedImage[jumpSpriteSheetCols];
        for (int i = 0; i < jumpSpriteSheetCols; i++) {
            jumpSprites[i] = jumpSpriteSheet.getSubimage(i * jumpSpriteSheetWidth, 0, jumpSpriteSheetWidth, jumpSpriteSheetHeight);
        }
    }
}
