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

    /**
     * Opens the main menu
     *
     * @param event the ActionEvent
     * @see ActionEvent
     */
    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    /**
     * Sets a new SceneChanger class.
     * Sets style of the info pane.
     *
     * @see SceneChanger
     */
    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
        infoPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
        infoPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

}
