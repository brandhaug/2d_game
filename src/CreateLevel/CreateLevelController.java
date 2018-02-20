package CreateLevel;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    private Image currentImage;

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
        currentImage = tile;
        updateGui();
    }

    @FXML
    public void chooseCoin() {
        toolEnabled = COIN_ENABLED;
        currentImage = coin;
        updateGui();
    }

    @FXML
    public void chooseEnemy() {
        toolEnabled = ENEMY_ENABLED;
        currentImage = mace;
        updateGui();

    }

    @FXML
    public void chooseEraser() {
        toolEnabled = ERASER_ENABLED;
        updateGui();
    }


    @FXML
    public void stepBackward() {
        if (steps.size() <= stepDiff) {
            System.out.println("Can't undo");
        } else {
            stepDiff++;
            Step step = steps.get(steps.size() - stepDiff);
            editCell(step.getX(), step.getY(), step.getLastValue(), false);
        }
    }

    @FXML
    public void stepForward() {
        if (stepDiff == 0) {
            System.out.println("Can't redo");
        } else {
            stepDiff--;
            Step step = steps.get(steps.size() - stepDiff - 1);
            editCell(step.getX(), step.getY(), step.getCurrentValue(), false);
        }
    }

    @FXML
    public void openLoadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load map");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());

        if (file != null) {
            map = MapParser.getArrayFromFile(file);
            render(false);
        }
    }

    @FXML
    public void openSaveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save map");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());

        if (file != null) {
            System.out.println(file);
            String content = getMapContent();
            saveFile(content, file);
        }
    }

    private String getMapContent() {
        StringBuilder content = new StringBuilder();

        int height = 100;
        int width = 1000;

        content.append("height: ");
        content.append(height);
        content.append(" width: ");
        content.append(width);

        for (int y = 0; y < map.length; y++) {
            content.append(System.lineSeparator());
            for (int x = 0; x < map[y].length; x++) {
                content.append(map[y][x]);
                content.append(" ");
            }
        }

        return content.toString();
    }

    private void saveFile(String content, File file) {
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGui() {
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
            int y = (int) (Math.floor((mouseEvent.getY() + currentOffsetY) / GRID_SIZE) + (map.length - (canvasHeight/GRID_SIZE)));
            int x = (int) (Math.floor((mouseEvent.getX() + currentOffsetX) / GRID_SIZE));
            editCell(x, y, toolEnabled, true);
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

    private void editCell(int x, int y, char tool, boolean addStep) {
        updateCurrentImage(tool);

        gc.clearRect((x * GRID_SIZE) - currentOffsetX + 2, canvasHeight - ((map.length - y) * GRID_SIZE) - currentOffsetY + 2, 61, 61);

        if (addStep) {
            addStep(x, y, tool);

        }

        map[y][x] = tool;

        if (tool == ERASER_ENABLED) {

        } else {
            renderCell(x, y);
        }
    }

    private void addStep(int x, int y, char tool) {
        while (stepDiff > 0) {
            stepDiff--;
            steps.remove(steps.size() - stepDiff - 1);
        }

        if (tool == map[y][x]) {
            System.out.println("Cant replace cell with the same object");
        } else {
            steps.add(new Step(x, y, tool, map[y][x]));
        }
    }

    private void updateCurrentImage(int tool) {
        if (tool == TILE_ENABLED) {
            currentImage = tile;
        } else if (tool == ENEMY_ENABLED) {
            currentImage = mace;
        } else if (tool == COIN_ENABLED) {
            currentImage = coin;
        }
    }

    public void render(boolean initialize) {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                updateCurrentImage(map[y][x]);
                gc.strokeRect((x * GRID_SIZE) - currentOffsetX, (canvasHeight - ((map.length - y) * GRID_SIZE) - currentOffsetY), GRID_SIZE, GRID_SIZE);
                if (initialize) {
                    map[y][x] = '0';
                } else if (map[y][x] != ERASER_ENABLED) {
                    renderCell(x, y);
                }
            }
        }
    }

    private void renderCell(int x, int y) {
        gc.drawImage(currentImage, (x * GRID_SIZE) - currentOffsetX, (canvasHeight - ((map.length - y) * GRID_SIZE) - currentOffsetY));
    }
}
