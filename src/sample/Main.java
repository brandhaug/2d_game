package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    public final static int WIDTH = 1280;
    public final static int HEIGHT = 720;
    public final static int BUTTON_SIZE = 64;
    public final static int MARGIN = 32;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("2D Game");

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image background = new Image("background.png");
        gc.drawImage(background, 0, 0);

        Image infoButton = new Image("info.png");

        gc.drawImage(infoButton, WIDTH - BUTTON_SIZE - MARGIN, MARGIN);

        Image playButton = new Image("play.png");
        gc.drawImage(playButton, WIDTH/2 - BUTTON_SIZE, HEIGHT/2);

        Image exitButton = new Image("exit.png");
        gc.drawImage(exitButton, WIDTH/2 + BUTTON_SIZE, HEIGHT/2);

        Image highscoreButton = new Image("highscore.png");
        gc.drawImage(highscoreButton, MARGIN, HEIGHT - BUTTON_SIZE - MARGIN);

        Image soundButton = new Image("sound_on.png");
        gc.drawImage(soundButton, WIDTH - BUTTON_SIZE * 2 - MARGIN * 2, HEIGHT - BUTTON_SIZE - MARGIN);

        Image musicButton = new Image("music_on.png");
        gc.drawImage(musicButton, WIDTH - BUTTON_SIZE - MARGIN, HEIGHT - BUTTON_SIZE - MARGIN);







//        gc.setFill(Color.RED);
//        Font mainFont = Font.font("Helvetica", FontWeight.BOLD, 48);
//        gc.setFont(mainFont);
//        gc.fillText("2D Game", 60, 50);


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
