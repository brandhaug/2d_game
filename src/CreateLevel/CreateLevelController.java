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


    private final int TILE_ENABLED = 0;
    private final int COIN_ENABLED = 1;
    private final int ENEMY_ENABLED = 2;
    private final int ERASER_ENABLED = 2;

    private int toolEnabled = 0;

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

    @FXML
    public void chooseTile() {
        toolEnabled = TILE_ENABLED;
        updateGui();
    }

    @FXML
    public void chooseCoin() {
        toolEnabled = COIN_ENABLED;
        updateGui();
    }

    @FXML
    public void chooseEnemy() {
        toolEnabled = ENEMY_ENABLED;
        updateGui();

    }

    @FXML
    public void chooseEraser(ActionEvent actionEvent) {
        toolEnabled = ERASER_ENABLED;
        updateGui();
    }

    @FXML
    public void openLoadFile() {
    }

    @FXML
    public void stepBackward(ActionEvent actionEvent) {
    }

    @FXML
    public void stepForward(ActionEvent actionEvent) {
    }

    @FXML
    public void openSaveFile(ActionEvent actionEvent) {
    }

    public void updateGui() {
        String border = "-fx-border-color: green;"
                + "-fx-border-width: 2;\n";
        String noBorder = "-fx-border-color: none;\n"
                + "-fx-border-width: 0;\n";

        if (toolEnabled == TILE_ENABLED) {
            tileButton.setStyle(tileButton.getStyle() + border);
        }
    }
}
