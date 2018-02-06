package Info;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoController {

    @FXML
    private Button mainMenuButton;
    @FXML
    private AnchorPane infoPane;

    @FXML
    protected void openMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../MainMenu/MainMenu.fxml"));
        root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        infoPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
        infoPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

}
