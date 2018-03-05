package MainMenu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainMenuController {

    @FXML
    private Pane mapsPane;
    @FXML
    private ImageView map1;
    @FXML
    private ImageView map2;
    @FXML
    private ImageView map3;
    @FXML
    private ImageView map4;
    @FXML
    private Button infoButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button highscoresButton;
    @FXML
    private Button soundButton;
    @FXML
    private Button musicButton;
    @FXML
    private Button createLevelButton;

    private Preferences preferences = Preferences.userRoot();

    private Clip musicClip;
    private AudioInputStream musicStream;

    private boolean initialized = false;

    @FXML
    protected void exit() {
        Platform.exit();
        System.exit(0);
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
            musicClip.start();
        } else {
            musicButton.setStyle("-fx-graphic: 'Resources/buttons/music_off.png'");
            musicClip.stop();
        }
    }

    @FXML
    protected void openGame() {
        changeScene("../Game/Game.fxml", true);
    }

    @FXML
    protected void openInfo() {
        changeScene("../Info/Info.fxml", true);
    }

    @FXML
    protected void openCreateLevel() {
        changeScene("../CreateLevel/CreateLevel.fxml", false);
    }

    @FXML
    protected void openHighscores() {
        changeScene("../Highscores/Highscores.fxml", true);
    }

    private void changeScene(String path, boolean loadStyleSheet) {
        Stage stage = (Stage) mapsPane.getScene().getWindow();

        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));

            if (loadStyleSheet) {
                root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
            }

            musicClip.stop();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        mapsPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
        mapsPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        try {
            initializeMusic();
            toggleMusic();
            toggleSound();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }

        map1.setOnMouseEntered(event -> map1.setImage(new Image("Resources/background/map1.gif")));
        map1.setOnMouseExited(event -> map1.setImage(new Image("Resources/background/map1_still_image.png")));
        map2.setOnMouseEntered(event -> map2.setImage(new Image("Resources/background/map1.gif")));
        map2.setOnMouseExited(event -> map2.setImage(new Image("Resources/background/map1_still_image.png")));
        map3.setOnMouseEntered(event -> map3.setImage(new Image("Resources/background/map1.gif")));
        map3.setOnMouseExited(event -> map3.setImage(new Image("Resources/background/map1_still_image.png")));
        map4.setOnMouseEntered(event -> map4.setImage(new Image("Resources/background/map1.gif")));
        map4.setOnMouseExited(event -> map4.setImage(new Image("Resources/background/map1_still_image.png")));
        initialized = true;
    }


    /**
     * Create an AudioInputStream from a given sound file
     * Acquire music format and create a DataLine.Infoobject
     * Obtain the Clip
     * Open the AudioInputStream and start playing
     *
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public void initializeMusic() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        musicStream = AudioSystem.getAudioInputStream(new File(getClass().getResource("/Resources/music/main_song.wav").getPath()));
        musicClip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, musicStream.getFormat()));
        musicClip.open(musicStream);
        musicClip.loop(10);
        musicClip.start();
    }
}
