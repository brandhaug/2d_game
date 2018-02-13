package CreateLevel;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CreateLevelController {

    @FXML private Canvas canvas;

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

}
