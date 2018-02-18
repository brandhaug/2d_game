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

import java.util.ArrayList;
import java.util.List;

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

    private int canvasWidth;
    private int canvasHeight;


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
    private int currentOffsetX = 0;
    private int currentOffsetY = 0;
    private int lastOffsetX = 0;
    private int lastOffsetY = 0;

    private boolean dragging = false;

    private List<Step> steps;
    private int stepDiff;


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
        if (steps.size() <= stepDiff){
            System.out.println("Can't undo");
        } else {
            stepDiff++;
            Step step = steps.get(steps.size() - stepDiff);
            editCell(step.getX(), step.getY(), step.getLastValue(), true);
        }
    }

    @FXML
    public void stepForward() {
        if (stepDiff == 0) {
            System.out.println("Can't redo");
        } else {
            stepDiff--;
            System.out.println(stepDiff);
            Step step = steps.get(steps.size() - stepDiff - 1);
            editCell(step.getX(), step.getY(), step.getCurrentValue(), true);
        }
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
            int y = (int) (Math.floor((mouseEvent.getY() + currentOffsetY) / GRID_SIZE));
            int x = (int) (Math.floor((mouseEvent.getX() + currentOffsetX) / GRID_SIZE));
            editCell(x, y, toolEnabled, false);
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        dragging = true;
        currentOffsetX = (int) (lastOffsetX + pressedX - mouseEvent.getX());
        currentOffsetY = (int) (lastOffsetY + pressedY - mouseEvent.getY());

        render(false);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        pressedX = (int) mouseEvent.getX();
        pressedY = (int) mouseEvent.getY();
        lastOffsetX = currentOffsetX;
        lastOffsetY = currentOffsetY;
    }

    @FXML
    public void initialize() {
        canvasHeight = (int) canvas.getHeight();
        canvasWidth = (int) canvas.getWidth();
        steps = new ArrayList<>();

        initializeSprites();

        gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);

        map = new char[100][1000];
        render(true);
    }

    private void initializeSprites() {
        mace = new Image("/Resources/buttons/mace.png");
        coin = new Image("/Resources/buttons/coin.png");
        tile = new Image("/Resources/buttons/tile.png");
    }

    private void editCell(int x, int y, char tool, boolean movingDiff) {
        gc.clearRect((x * GRID_SIZE) - currentOffsetX + 2, (y * GRID_SIZE) - currentOffsetY + 2, 61, 61);
        System.out.println("Edit " + stepDiff);

        if (!movingDiff) {
            while (stepDiff > 0) {
                stepDiff--;
                System.out.println("Removing " + stepDiff);
                steps.remove(steps.size() - stepDiff - 1);
            }

            steps.add(new Step(x, y, toolEnabled, map[y][x]));
        } else {
//            System.out.println("Moving " + stepDiff);
        }

        map[y][x] = tool;

        if (tool == ERASER_ENABLED) {

        } else {
            gc.drawImage(imageEnabled, (x * GRID_SIZE) - currentOffsetX, (y * GRID_SIZE) - currentOffsetY);
        }
    }

    public void render(boolean initialize) {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        for (int y = map.length - 1; y > 0; y--) {
            for (int x = 0; x < map[y].length; x++) {
                gc.strokeRect((x * GRID_SIZE) - currentOffsetX, (canvasHeight - ((map.length - y) * GRID_SIZE) - currentOffsetY), GRID_SIZE, GRID_SIZE);
                if (initialize) {
                    map[y][x] = '0';
                } else {
                    if (map[y][x] == TILE_ENABLED) {
                        gc.drawImage(tile, (x * GRID_SIZE) - currentOffsetX, (y * GRID_SIZE) - currentOffsetY);
                    } else if (map[y][x] == ENEMY_ENABLED) {
                        gc.drawImage(mace, (x * GRID_SIZE) - currentOffsetX, (y * GRID_SIZE) - currentOffsetY);
                    } else if (map[y][x] == COIN_ENABLED) {
                        gc.drawImage(coin, (x * GRID_SIZE) - currentOffsetX, (y * GRID_SIZE) - currentOffsetY);
                    }
                }
            }
        }
    }
}
