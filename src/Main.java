import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public final static int WIDTH = 1280;
    public final static int HEIGHT = 720;
    public final static int BUTTON_SIZE = 64;
    public final static int MARGIN = 32;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("2D Game");

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
