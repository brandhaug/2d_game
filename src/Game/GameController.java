package Game;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class GameController {

    public final static int WIDTH = 1280;
    public final static int HEIGHT = 720;
    public final static int BUTTON_SIZE = 64;
    public final static int MARGIN = 32;

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

    private boolean running = false;

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
            pauseButton.setStyle("-fx-graphic: 'file:res/images/buttons/pause.png'");
        } else {
            pauseButton.setStyle("-fx-graphic: 'file:res/images/buttons/play.png'");
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
            soundButton.setStyle("-fx-graphic: 'file:res/images/buttons/sound_on.png'");
        } else {
            soundButton.setStyle("-fx-graphic: 'file:res/images/buttons/sound_off.png'");
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
            musicButton.setStyle("-fx-graphic: 'file:res/images/buttons/music_on.png'");
        } else {
            musicButton.setStyle("-fx-graphic: 'file:res/images/buttons/music_off.png'");
        }
    }

    @FXML
    public void initialize() {
        musicButton.setVisible(false);
        soundButton.setVisible(false);
        mainMenuButton.setVisible(false);
        initialized = true;
        GraphicsContext gc = canvas.getGraphicsContext2D();

        final long startNanoTime = System.nanoTime();

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gameLoop(gc, startNanoTime, currentNanoTime);
            }
        }.start();
        gc.drawImage(new Image("file:res/images/background.png"), 0, 0, WIDTH, HEIGHT);
        gc.drawImage(new Image("file:res/images/tiles/128/Dirt.png"), 128, 128);
    }

    private void gameLoop(GraphicsContext gc, long startNanoTime, long currentNanoTime) {
        gc.clearRect(0, 0, 1280, 720);
        gc.drawImage(new Image("file:res/images/background.png"), 0, 0, 1280, 720);
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;

        double x = 232 + 128 * Math.cos(t);
        double y = 232 + 128 * Math.sin(t);
        gc.drawImage(new Image("file:res/images/tiles/128/Dirt.png"), x, y );
    }

}
