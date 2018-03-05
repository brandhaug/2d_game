package Highscores;

import SceneChanger.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HighscoresController {

    @FXML
    private Button mainMenuButton;

    private SceneChanger sceneChanger;

    @FXML
    protected void openMainMenu(ActionEvent event) throws IOException {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
    }

}
