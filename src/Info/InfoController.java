package Info;

import SceneChanger.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class InfoController {

    @FXML
    private Button mainMenuButton;
    @FXML
    private AnchorPane infoPane;

    private SceneChanger sceneChanger;

    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();

        //TODO: Sett styles i FXML eller stylesheet
        infoPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
        infoPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

}
