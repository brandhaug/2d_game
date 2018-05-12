import MainMenu.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Starts the application
     * @param primaryStage the stage the application is being run on
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Red Runner");
        MainMenuController mainMenuController = new MainMenuController();
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu/MainMenu.fxml"));
        root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
    }


    /**
     * Starts the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
