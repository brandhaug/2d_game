package MainMenu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class MainMenuController {

    @FXML
    private Button infoButton;

    @FXML
    private Button playButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button highscoresButton;

    @FXML
    private Button soundButton;

    @FXML
    private Button musicButton;

    private Preferences preferences = Preferences.userRoot();
    private boolean initialized = false;

    @FXML
    protected void openInfo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Info/Info.fxml"));
        root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void play() {
        try {
            Stage stage = (Stage) playButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../Game/Game.fxml"));
            root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void openHighscores(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Highscores/Highscores.fxml"));
        root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
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
    public void initialize() {
        toggleSound();
        toggleMusic();
        initialized = true;
    }

}
