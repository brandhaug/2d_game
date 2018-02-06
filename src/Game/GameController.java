package Game;

import Game.Levels.Beginner;
import Game.Player.Player;
import javafx.animation.AnimationTimer;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class GameController {

    public final static int WIDTH = 1280;
    public final static int HEIGHT = 720;
    public final static int BUTTON_SIZE = 64;
    public final static int TILE_SIZE = 128;
    public final static int MARGIN = 32;
    private Beginner level;
    private Player player;

    private Preferences preferences = Preferences.userRoot();
    private boolean gamePaused = false;
    private boolean initialized = false;

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
    public void pause() {
        if (gamePaused) {
            pauseButton.setStyle("-fx-graphic: 'Resources/buttons/pause.png'");
        } else {
            pauseButton.setStyle("-fx-graphic: 'Resources/buttons/play.png'");
        }
        soundButton.setVisible(!gamePaused);
        musicButton.setVisible(!gamePaused);
        mainMenuButton.setVisible(!gamePaused);
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
        } else {
            soundButton.setStyle("-fx-graphic: 'Resources/buttons/sound_off.png'");
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

        if (code == KeyCode.RIGHT || code == KeyCode.D) player.setVelocityX(player.getVelocityX() + 2);
        if (code == KeyCode.LEFT || code == KeyCode.A) player.setVelocityX(player.getVelocityX() -2);
        if (code == KeyCode.UP || code == KeyCode.W) player.setVelocityY(player.getVelocityY() - 5);
    }

    @FXML
    private void handleKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();

        if (code == KeyCode.RIGHT || code == KeyCode.D) player.setVelocityX(0);
        if (code == KeyCode.LEFT || code == KeyCode.A) player.setVelocityX(0);
        if (code == KeyCode.UP || code == KeyCode.W) player.setVelocityY(10);
    }


    @FXML
    public void initialize() {
        musicButton.setVisible(false);
        soundButton.setVisible(false);
        mainMenuButton.setVisible(false);
        initialized = true;
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.drawImage(new Image("Resources/background.png"), 0, 0, WIDTH, HEIGHT);
        level = new Beginner(gc);
        player = new Player(gc);
        player.setX(128);
        player.setY(128);

        final long startNanoTime = System.nanoTime();

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gameLoop(gc, startNanoTime, currentNanoTime, player, level);
            }
        }.start();
    }

    private void gameLoop(GraphicsContext gc, long startNanoTime, long currentNanoTime, Player player, Beginner level) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        gc.drawImage(new Image("Resources/background.png"), 0, 0, WIDTH, HEIGHT);

        double t = (currentNanoTime - startNanoTime) / 1000000000.0;

        level.draw(gc);
        player.tick(gc);


//        double x = 232 + 128 * Math.cos(t);
//        double y = 232 + 128 * Math.sin(t);
//        gc.drawImage(new Image("Resources/images/tiles/128/Dirt.png"), x, y);

    }

}