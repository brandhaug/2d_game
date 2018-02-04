package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML private Button infoButton;
    @FXML private Button playButton;
    @FXML private Button exitButton;
    @FXML private Button highscoresButton;
    @FXML private Button soundButton;
    @FXML private Button musicButton;


    @FXML
    protected void openInfo() {
        System.out.println("Info");
    }

    @FXML
    protected void play() {
        System.out.println("Play");
    }

    @FXML
    protected void exit() {
        System.out.println("Exit");
    }

    @FXML
    protected void openHighscores() {
        System.out.println("Highscors");
    }

    @FXML
    protected void toggleSound() {
        System.out.println("Sound");
    }

    @FXML
    protected void toggleMusic() {
        System.out.println("Music");
    }

}
