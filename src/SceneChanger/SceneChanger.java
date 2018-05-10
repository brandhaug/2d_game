package SceneChanger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChanger {

    public void changeScene(ActionEvent event, String path, boolean loadStyleSheet) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        changeScene(path, loadStyleSheet, stage);
    }

    private void changeScene(String path, boolean loadStyleSheet, Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));

            if (loadStyleSheet) {
                root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
            }

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeScene(Stage stage, String path, boolean loadStyleSheet) {
        changeScene(path, loadStyleSheet, stage);
    }
}
