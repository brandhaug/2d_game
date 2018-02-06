package Game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.prefs.Preferences;

public class GameController {

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
    protected void exit() {
        Platform.exit();
        System.exit(0);
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
        initialized = true;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(new Image("file:res/images/background.png"), 0, 0, 1280, 720);
        gc.drawImage(new Image("file:res/images/tiles/128/Dirt.png"), 0, 0, 128, 128);
    }
}
