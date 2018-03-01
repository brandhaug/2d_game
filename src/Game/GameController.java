package Game;

import Game.GameObjects.Bullet;
import Game.GameObjects.Player;
import Game.Levels.Level;
import Resources.soundEffects.SoundEffects;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.w3c.dom.events.MutationEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.Preferences;

import static Resources.soundEffects.SoundEffects.mute;

public class GameController {

    public final static int CANVAS_WIDTH = 1280;
    public final static int CANVAS_HEIGHT = 720;
    public final static int BUTTON_SIZE = 64;
    public final static int TILE_SIZE = 128;
    public final static int MARGIN = 32;
    public final static int PLAYER_X_MARGIN = 200;
    public final static int PLAYER_Y_MARGIN = 250;

    private Level level;
    private Player player;
    private CollisionHandler collisionHandler;
    private SoundEffects soundEffects;
    private Preferences preferences = Preferences.userRoot();
    private int coinAmount;
    private int bulletAmount;

    private Image background;
    private GraphicsContext gc;

    private boolean gamePaused = false;
    private boolean initialized = false;
    private boolean gameOver = false;

    @FXML
    private Canvas canvas;
    @FXML
    private Button pauseButton;
    @FXML
    private Button soundButton;
    @FXML
    private Button musicButton;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button gameOverMainMenuButton;
    @FXML
    private Button gameOverRetryButton;
    @FXML
    private Pane gameOverPane;
    @FXML
    private Pane pauseInfoPane;
    @FXML
    private Pane pauseSettingsPane;
    @FXML
    private Text gameOverText;
    @FXML
    private Text pauseText;

    @FXML
    protected void openMainMenu() {
        try {
            Stage stage = (Stage) mainMenuButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../MainMenu/MainMenu.fxml"));
            root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void restartLevel() {
        try {
            Stage stage = (Stage) canvas.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../Game/Game.fxml"));
            root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pause() {
        if (gamePaused) {
            pauseButton.setStyle("-fx-graphic: 'Resources/buttons/pause.png'");
        } else {
            pauseButton.setStyle("-fx-graphic: 'Resources/buttons/play.png'");
        }
        soundButton.setVisible(!gamePaused);
        musicButton.setVisible(!gamePaused);
        mainMenuButton.setVisible(!gamePaused);
        pauseInfoPane.setVisible(!gamePaused);
        pauseText.setVisible(!gamePaused);
        pauseSettingsPane.setVisible(!gamePaused);
        gamePaused = !gamePaused;
    }

    @FXML
    protected void toggleSound() {
        boolean soundOn = preferences.getBoolean("sound", true);

        if (initialized) {
            preferences.putBoolean("sound", !soundOn);
            soundOn = !soundOn;
        }

        if (soundOn) {
            soundButton.setStyle("-fx-graphic: 'Resources/buttons/sound_on.png'");
            mute = false;
        } else {
            soundButton.setStyle("-fx-graphic: 'Resources/buttons/sound_off.png'");
            mute = true;
        }
    }

    @FXML
    protected void toggleMusic() {
        boolean musicOn = preferences.getBoolean("music", true);

        if (initialized) {
            preferences.putBoolean("music", !musicOn);
            musicOn = !musicOn;
        }

        if (musicOn) {
            musicButton.setStyle("-fx-graphic: 'Resources/buttons/music_on.png'");
        } else {
            musicButton.setStyle("-fx-graphic: 'Resources/buttons/music_off.png'");
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        if (!gamePaused && !gameOver) {
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                player.setVelocityX(10, false);
            }
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                player.setVelocityX(-10, false);
            }
            if ((code == KeyCode.UP || code == KeyCode.W) && player.getVelocityY() == 0) {
                player.setVelocityY(-35);
                soundEffects.JUMP.play();
            }

            if ((code == KeyCode.E)) {
                if(bulletAmount > 0){
                    if(player.getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT || player.getCurrentSpriteState() == Player.PLAYER_FALLING_RIGHT||
                            player.getCurrentSpriteState() == Player.PLAYER_RUNNING_RIGHT || player.getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT) {
                        level.addBullet(new Bullet(PLAYER_X_MARGIN+20,player.getY()+20,1));
                    }else{
                        level.addBullet(new Bullet(PLAYER_X_MARGIN+20,player.getY()+20,-1));
                    }
                    bulletAmount--;
                }
            }
        }
    }

    @FXML
    private void handleKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();

        if (!gamePaused && !gameOver) {
            if ((code == KeyCode.RIGHT || code == KeyCode.D && player.getVelocityX() > 0)) {
                player.setVelocityX(0, false);
                player.setRightCollision(false);
            }
            if (code == KeyCode.LEFT || code == KeyCode.A && player.getVelocityX() < 0) {
                player.setVelocityX(0, false);
                player.setLeftCollision(false);
            }
            if (code == KeyCode.LEFT || code == KeyCode.A && player.getVelocityX() < 0) {
                player.setVelocityX(0,false);
                player.setLeftCollision(false);
            }
        }
    }

    @FXML
    public void initialize() {
        initializeBackground();
        level = new Level("survival");
        gc = canvas.getGraphicsContext2D();
        player = new Player(level.getPlayerStartPositionX(), level.getPlayerStartPositionY());
        coinAmount = level.getCoins().size();
        bulletAmount = level.getBulletCounter();
        //player = new Player(Player.START_POSITION_X, Player.START_POSITION_Y, EnemyType.ENEMYTEST, EnemyType.ENEMYTEST2);
        //player = new Player(Player.START_POSITION_X, Player.START_POSITION_Y);
        collisionHandler = new CollisionHandler(player, level, soundEffects);

        final long startNanoTime = System.nanoTime();
        initialized = true;

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (!gamePaused && !gameOver) {
                    gameLoop(startNanoTime, currentNanoTime);
                }
            }
        }.start();
    }

    //Find better running sound before using this method
    private void playerMoving() {
        if (player.getCurrentSpriteState() == 2 || player.getCurrentSpriteState() == 3) soundEffects.RUN.playLoop();

        if (player.getCurrentSpriteState() < 2 || player.getCurrentSpriteState() > 3 || gameOver)
            soundEffects.RUN.stopLoop();
    }

    private void gameLoop(long startNanoTime, long currentNanoTime) {
        double time = (currentNanoTime - startNanoTime) / 1000000000.0;

        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        drawBackground(gc);
        player.handleSpriteState();
        collisionHandler.tick();
        level.tick(gc, player, time);
        player.tick(gc);
        //playerMoving();
        //TODO: Make tick for GUI()
        gc.setFill(Color.BLACK);
        gc.fillText(level.getCoinCounter() + "/" + coinAmount,60,40);
        checkGameOver();
        //checkPlayerBoundry();
    }

    int playerX = 256;

    private void bulletPlacementX(){

    }

    /*
    private void checkPlayerBoundry(){
        if(player.getX() > 830){
            player.setVelocityX(0);
            player.setX(829);
        }
    }
    */

    private void checkGameOver() {
        if (player.getY() >= level.getLowestTileY() || !player.getAlive()) {
            gameOver = true;
            gameOverPane.setVisible(true);
            gameOverText.setVisible(true);
            gameOverMainMenuButton.setVisible(true);
            gameOverRetryButton.setVisible(true);
            canvas.setOpacity(0.7f);
            soundEffects.GAMEOVER.play();
        }
    }

    private void initializeBackground() {
        try {
            BufferedImage bufferedBackground = ImageIO.read(new File(getClass().getResource("/Resources/background/background.png").getPath()));
            background = SwingFXUtils.toFXImage(bufferedBackground, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawBackground(GraphicsContext gc) {
        int tempX = player.getX();
        while (tempX > CANVAS_WIDTH + player.getStartPosition()) {
            tempX -= CANVAS_WIDTH;
        }

        gc.drawImage(background, player.getStartPosition() - tempX, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.drawImage(background, CANVAS_WIDTH + player.getStartPosition() - tempX, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }
}