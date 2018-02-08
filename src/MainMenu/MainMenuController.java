package MainMenu;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    private Preferences preferences = Preferences.userRoot();
    private boolean initialized = false;

    @FXML
    protected void openInfo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Info/Info.fxml"));
        root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void play() {
        try {
            Stage stage = (Stage) mapsPane.getScene().getWindow();
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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
        mapsPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
        mapsPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        toggleSound();
        toggleMusic();
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

}
