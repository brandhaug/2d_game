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
    private String mapName = "game";

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

    /**
     * Opens the main menu scene.
     * @param event the action event given from the button click.
     */
    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    /**
     * Opens the game scene.
     * @param event the action event given from the button click.
     */
    @FXML
    protected void restartLevel(ActionEvent event) {
        sceneChanger.changeScene(event, "../Game/Game.fxml", true);
    }

    /**
     * Opens the high score scene.
     * @param event the action event given from the button click.
     */
    @FXML
    protected void openHighScores(ActionEvent event) {
        sceneChanger.changeScene(event, "../Highscores/Highscores.fxml", true);
    }

    /**
     * Sets sound on/off when toggled and changes the icon. Also initiates sound according to the users previous preferences.
     */
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

    /**
     * Sets music on/off when toggled and changes the icon. Also initiates music according to the users previous preferences.
     */
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

    /**
     * Handles different keys on press, if game is running.
     * Moves player right on RIGHT or D.
     * Moves player left on LEFT or A.
     * Makes player jump on UP or W.
     * Shoots bullet on E, if player has more bullets.
     * @param event the event given from a key press.
     */
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

    /**
     * Handles different keys on release if game is running.
     * Sets player's velocityX to 0 on RIGHT or D.
     * Sets player's velocityX to 0 on LEFT or A.
     * @param event the key event given by a key release.
     */
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
        }
    }

    /**
     * Initializes necessary variables and object of the game.
     * Starts an AnimationTimer which keeps the game running by calling on gameLoop method.
     * @see AnimationTimer
     */
    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
        initializeBackground();
        level = new Level(mapName);
        gc = canvas.getGraphicsContext2D();
        player = new Player(level.getPlayerStartPositionX(), level.getPlayerStartPositionY());
        coinAmount = level.getCoins().size();
        bulletAmount = level.getBulletCounter();
        collisionHandler = new CollisionHandler(player, level);
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

    /**
     * Start the players run sound, if the player is running.
     */
    private void setPlayerRunningSound() {
        if (player.isRunning()) {
            SoundEffects.RUN.playLoop();
        } else if (!player.isRunning() || gameOver) {
            SoundEffects.RUN.stopLoop();
        }
    }

    /**
     * Keeps the game running by drawing canvas according to state of the game.
     * @param startNanoTime the nano time of when the game was initialized.
     * @param currentNanoTime the nano time of the current game loop.
     */
    private void gameLoop(long startNanoTime, long currentNanoTime) {
        double time = (currentNanoTime - startNanoTime) / 1000000000.0;

        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        renderBackground();
        player.handleSpriteState();
        collisionHandler.tick();
        level.tick(gc, player, time);
        player.tick(gc);
        setPlayerRunningSound();
        renderGUI();
        checkGameOver();
        checkGameWon();
    }

    /**
     * Renders the GUI.
     */
    private void renderGUI() {
        gc.setFill(Color.BLACK);
        gc.fillText("Bullets: " + bulletAmount, 250, 40);
        gc.fillText(bulletAmount + "/" + coinAmount, 60, 40);
        renderTime();
        drawHealthBar();
    }

    /**
     * Sets game over to true if players y-coordinate is below the lowest tile or if player is dead.
     */
    private void checkGameOver() {
        if (player.getY() >= level.getLowestTileY() || !player.getAlive()) {
            gameOver = true;
            gameOverPane.setVisible(true);
            canvas.setOpacity(0.7f);
            SoundEffects.GAMEOVER.play();
        }
    }

    /**
     * Sets game won to true if the player has reached the chest.
     */
    private void checkGameWon() {
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

    /**
     * Draws the health bar.
     */
    private void drawHealthBar() {
        short healthX = 100;
        short healthY = 24;
        short healthWidth = 80;
        short healthHeight = 25;
        short healthArc = 10;

        // Health bar background
        gc.setGlobalAlpha(0.7);
        gc.setFill(Color.DARKGRAY);
        gc.fillRoundRect(healthX, healthY, healthWidth, healthHeight, healthArc, healthArc);

        // Health bar fill
        gc.setGlobalAlpha(1);
        if (player.getHp() >= 90) {
            gc.setFill(Color.LIMEGREEN);
        } else if (player.getHp() >= 75) {
            gc.setFill(Color.YELLOW);
        } else if (player.getHp() >= 50) {
            gc.setFill(Color.ORANGE);
        } else {
            gc.setFill(Color.RED);
        }
        gc.fillRoundRect(healthX, healthY, player.getHp() / 1.25, healthHeight, healthArc, healthArc);

        // Health bar stroke
        gc.setGlobalAlpha(0.5);
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(healthX, healthY, healthWidth, healthHeight, healthArc, healthArc);

        // Health bar text
        gc.setGlobalAlpha(1);
        gc.setFill(Color.BLACK);
        String formattedHp = String.valueOf((double) player.getHp() / 100 * 100);
        gc.fillText(formattedHp.substring(0, formattedHp.length() - 2) + "%", 187, 40);
    }

    /**
     * Sets pause to on/off when toggled and changes the icon accordingly.
     */
    @FXML
    public void togglePause() {
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

    /**
     * Draws the game time.
     */
    private void renderTime() {
        timeSeconds = (int) stopWatch.getTime() / 1000;
        String formattedTime = String.valueOf(timeSeconds);

        gc.setFont(bigFont);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText(formattedTime, CANVAS_WIDTH - 120, 60);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(smallFont);
    }

    /**
     * Initializes the background image.
     */
    private void initializeBackground() {
        try {
            BufferedImage bufferedBackground = ImageIO.read(new File(getClass().getResource("/Resources/background/background.png").getPath()));
            background = SwingFXUtils.toFXImage(bufferedBackground, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the background by rendering two images and moving them according to players x position.
     */
    private void renderBackground() {
        int tempX = player.getX();

        while (tempX > CANVAS_WIDTH + player.getStartPosition()) {
            tempX -= CANVAS_WIDTH;
        }

        gc.drawImage(background, player.getStartPosition() - tempX, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.drawImage(background, CANVAS_WIDTH + player.getStartPosition() - tempX, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }
}