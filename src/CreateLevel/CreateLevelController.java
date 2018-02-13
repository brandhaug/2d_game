package CreateLevel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class CreateLevelController {

    @FXML
    private Canvas canvas;
    @FXML
    Button tileButton;
    @FXML
    Button enemyButton;
    @FXML
    Button coinButton;
    @FXML
    Button loadButton;

    private GraphicsContext gc;
    private final int GRID_SIZE = 128;

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);


        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 1000; x++) {
                gc.strokeRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }
        }
    }

    public void chooseTile() {
    }

    public void chooseCoin() {
    }

    public void chooseEnemy() {
    }

    public void openLoadFile() {
    }
}
