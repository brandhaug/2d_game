package Game;

import Game.GameObjects.Bullet;
import Game.GameObjects.Player;
import Game.Levels.Level;
import Highscores.HighscoreHandler;
import Resources.soundEffects.SoundEffects;
import SceneChanger.SceneChanger;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.time.StopWatch;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import static Resources.soundEffects.SoundEffects.mute;

public class GameController {

    /**
     * FXML Elements
     */
    @FXML
    private Canvas canvas;
    @FXML
    private ImageView gameWonCoinImage;
    @FXML
    private ImageView gameWonTimerImage;
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
    private Button gameWonMainMenuButton;
    @FXML
    private Button gameWonRetryButton;
    @FXML
    private Button gameWonHighScoresButton;
    @FXML
    private Pane gameOverPane;
    @FXML
    private Pane pauseInfoPane;
    @FXML
    private Pane pauseSettingsPane;
    @FXML
    private Pane gameWonPane;
    @FXML
    private Text gameWonCoins;
    @FXML
    private Text gameWonTime;
    @FXML
    private Text gameWonHighScore;
    @FXML
    private Text gameOverText;
    @FXML
    private Text pauseText;

    /**
     * FXML elements size
     */
    public final static int CANVAS_WIDTH = 1280;
    public final static int CANVAS_HEIGHT = 720;
    public final static int BUTTON_SIZE = 64;
    public final static int TILE_SIZE = 72;
    public final static int MARGIN = 32;

    /**
     * Player and map settings
     */
    public final static int PLAYER_X_MARGIN = 500;
    public final static int PLAYER_Y_MARGIN = 250;
    private String mapName = "test2.txt";

    /**
     * Classes
     */
    private Level level;
    private Player player;
    private CollisionHandler collisionHandler;
    private SoundEffects soundEffects;
    private SceneChanger sceneChanger;
    private HighscoreHandler highscoreHandler;
    private Preferences preferences = Preferences.userRoot();

    /**
     * Score
     */
    private int coinAmount;
    private int bulletAmount;
    private int timeSeconds;
    private StopWatch stopWatch;

    /**
     * GUI
     */
    private Font smallFont = new Font("Calibri", 14);
    private Font bigFont = new Font("Calibri", 40);

    /**
     * Canvas
     */
    private Image background;
    private GraphicsContext gc;

    /**
     * Game states
     */
    private boolean gamePaused = false;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean initialized = false;

    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    @FXML
    protected void restartLevel(ActionEvent event) {
        sceneChanger.changeScene(event, "../Game/Game.fxml", true);
    }

    @FXML
    protected void openHighScores(ActionEvent event) {
        sceneChanger.changeScene(event, "../Highscores/Highscores.fxml", true);
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
                if (bulletAmount > 0) {
                    if (player.getCurrentSpriteState() == Player.PLAYER_IDLING_RIGHT || player.getCurrentSpriteState() == Player.PLAYER_FALLING_RIGHT ||
                            player.getCurrentSpriteState() == Player.PLAYER_RUNNING_RIGHT || player.getCurrentSpriteState() == Player.PLAYER_JUMPING_RIGHT) {
                        level.addBullet(new Bullet(PLAYER_X_MARGIN + 20, player.getY() + 20, 1, 1));
                    } else {
                        level.addBullet(new Bullet(PLAYER_X_MARGIN + 20, player.getY() + 20, 1, -1));
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
                player.setVelocityX(0, false);
                player.setLeftCollision(false);
            }
        }
    }

    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
        initializeBackground();
        level = new Level(mapName);
        gc = canvas.getGraphicsContext2D();
        player = new Player(level.getPlayerStartPositionX(), level.getPlayerStartPositionY());
        coinAmount = level.getCoins().size();
        bulletAmount = level.getBulletCounter();
        collisionHandler = new CollisionHandler(player, level, soundEffects);
        highscoreHandler = new HighscoreHandler();

        final long startNanoTime = System.nanoTime();
        initialized = true;
        stopWatch = new StopWatch();
        stopWatch.start();

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (!gamePaused && !gameOver && !gameWon) {
                    gameLoop(startNanoTime, currentNanoTime);
                }
            }
        }.start();
    }

    private void playerMoving() {
        if (player.isRunning()) {
            soundEffects.RUN.playLoop();
        } else if (!player.isRunning() || gameOver) {
            soundEffects.RUN.stopLoop();
        }
    }

    private void gameLoop(long startNanoTime, long currentNanoTime) {
        double time = (currentNanoTime - startNanoTime) / 1000000000.0;

        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        renderBackground(gc);
        player.handleSpriteState();
        collisionHandler.tick();
        level.tick(gc, player, time);
        player.tick(gc);
        playerMoving();
        renderGUI();
        checkGameOver();
        checkGameWon();
    }

    private void renderGUI() {
        gc.setFill(Color.BLACK);
        gc.fillText("Bullets: " + bulletAmount, 250, 40);
        gc.fillText(bulletAmount + "/" + coinAmount, 60, 40);
        renderTime();
    }

    private void checkGameOver() {
        if (player.getY() >= level.getLowestTileY() || !player.getAlive()) {
            gameOver = true;
            gameOverPane.setVisible(true);
            canvas.setOpacity(0.7f);
            soundEffects.GAMEOVER.play();
        }
    }

    public void checkGameWon() {
        if (level.getChest().isAnimated()) {
            gameWon = true;
            gameWonPane.setVisible(true);
            gameWonCoins.setText(gameWonCoins.getText() + String.valueOf(level.getCoins().size()));
            gameWonTime.setText(gameWonTime.getText() + String.valueOf(timeSeconds));
            if (highscoreHandler.isNewHighscore(mapName, timeSeconds, coinAmount)) {
                gameWonHighScore.setText("Congratulations, it's a new high score!");
                highscoreHandler.addToHighscore(mapName, timeSeconds, coinAmount);
            }
            canvas.setOpacity(0.7f);
        }
    }

    @FXML
    public void pause() {
        if (gamePaused) {
            pauseButton.setStyle("-fx-graphic: 'Resources/buttons/pause.png'");
            stopWatch.resume();
        } else {
            pauseButton.setStyle("-fx-graphic: 'Resources/buttons/play.png'");
            stopWatch.suspend();
        }
        soundButton.setVisible(!gamePaused);
        musicButton.setVisible(!gamePaused);
        mainMenuButton.setVisible(!gamePaused);
        pauseInfoPane.setVisible(!gamePaused);
        pauseSettingsPane.setVisible(!gamePaused);
        gamePaused = !gamePaused;
    }

    private void renderTime() {
        timeSeconds = (int) stopWatch.getTime() / 1000;
        String formattedTime = String.valueOf(timeSeconds);

        gc.setFont(bigFont);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText(formattedTime, CANVAS_WIDTH - 120, 60);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(smallFont);
    }

    private void initializeBackground() {
        try {
            BufferedImage bufferedBackground = ImageIO.read(new File(getClass().getResource("/Resources/background/background.png").getPath()));
            background = SwingFXUtils.toFXImage(bufferedBackground, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderBackground(GraphicsContext gc) {
        int tempX = player.getX();

        while (tempX > CANVAS_WIDTH + player.getStartPosition()) {
            tempX -= CANVAS_WIDTH;
        }

        gc.drawImage(background, player.getStartPosition() - tempX, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.drawImage(background, CANVAS_WIDTH + player.getStartPosition() - tempX, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }
}