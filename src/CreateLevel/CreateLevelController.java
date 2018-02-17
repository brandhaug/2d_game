package CreateLevel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
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
    private final int GRID_SIZE = 64;

    private char[][] map;

    private final int CANVAS_HEIGHT = 640;
    private final int CANVAS_WIDTH = 1280;


    private final char TILE_ENABLED = '-';
    private final char COIN_ENABLED = 'c';
    private final char ENEMY_ENABLED = 'e';
    private final char ERASER_ENABLED = '0';

    private char toolEnabled = '0';

    private Image mace;
    private Image tile;
    private Image coin;
    private Image imageEnabled;

    private int pressedX;
    private int pressedY;
    private int offsetX;
    private int offsetY;

    private boolean dragging = false;


    @FXML
    public void chooseTile() {
        toolEnabled = TILE_ENABLED;
        imageEnabled = tile;
        updateGui();
    }

    @FXML
    public void chooseCoin() {
        toolEnabled = COIN_ENABLED;
        imageEnabled = coin;
        updateGui();
    }

    @FXML
    public void chooseEnemy() {
        toolEnabled = ENEMY_ENABLED;
        imageEnabled = mace;
        updateGui();

    }

    @FXML
    public void chooseEraser() {
        toolEnabled = ERASER_ENABLED;
        updateGui();
    }


    @FXML
    public void stepBackward() {
        System.out.println("back");
    }

    @FXML
    public void stepForward() {
        System.out.println("forward");
    }

    @FXML
    public void openLoadFile() {
        System.out.println("load");
    }

    @FXML
    public void openSaveFile() {
        System.out.println("save");
    }

    public void updateGui() {
        System.out.println(toolEnabled);
//        String border = "-fx-border-color: green;"
//                + "-fx-border-width: 2;\n";
//        String noBorder = "-fx-border-color: none;\n"
//                + "-fx-border-width: 0;\n";
//
//        if (toolEnabled == TILE_ENABLED) {
//            tileButton.setStyle(tileButton.getStyle() + border);
//        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (dragging) {
            dragging = false;
        } else {
            int y = (int) (Math.floor(mouseEvent.getY() / GRID_SIZE));
            int x = (int) (Math.floor(mouseEvent.getX() / GRID_SIZE));
            System.out.println("Clicked");
            editCell(x, y);
        }
    }


    private void editCell(int x, int y) {
        gc.clearRect(x * GRID_SIZE + 2  - offsetX, y * GRID_SIZE + 2  - offsetX, 61, 61);
        map[y][x] = toolEnabled;

        if (toolEnabled == ERASER_ENABLED) {

        } else {
            gc.drawImage(imageEnabled, (x * GRID_SIZE) - offsetX, (y * GRID_SIZE) - offsetY);
        }
    }

    @FXML
    public void initialize() {
        initializeSprites();

        gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);

        map = new char[100][1000];

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                gc.strokeRect((x * GRID_SIZE), (y * GRID_SIZE), GRID_SIZE, GRID_SIZE);
                map[y][x] = '0';
            }
        }
    }

    private void initializeSprites() {
        mace = new Image("/Resources/buttons/mace.png");
        coin = new Image("/Resources/buttons/coin.png");
        tile = new Image("/Resources/buttons/tile.png");
    }


    public void mouseDragged(MouseEvent mouseEvent) {
            dragging = true;
            offsetX += (int) (pressedX - mouseEvent.getX());
            offsetY += (int) (pressedY - mouseEvent.getY());

        render(offsetX, offsetY);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        pressedX = (int) mouseEvent.getX();
        pressedY = (int) mouseEvent.getY();
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void render(int offsetX, int offsetY) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                gc.strokeRect((x * GRID_SIZE) - offsetX, (y * GRID_SIZE) - offsetY, GRID_SIZE, GRID_SIZE);
            }
        }
    }
}
