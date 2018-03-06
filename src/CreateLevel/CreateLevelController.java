package CreateLevel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    Button playerButton;
    @FXML
    Button chestButton;
    @FXML
    Button loadButton;
    @FXML
    private Button eraseButton;

    private char[][] map;

    /**
     * Tools
     */
    private final char TILE_ENABLED = '-';
    private final char COIN_ENABLED = 'c';
    private final char ENEMY_ENABLED = 'e';
    private final char STARTING_POINT_ENABLED = 's';
    private final char END_POINT_ENABLED = 'f';
    private final char ERASER_ENABLED = '0';

    private char toolEnabled = '0';

    /**
     * Tool monitoring
     */
    private boolean startingPointExists = false;
    private boolean endPointExists = false;

    /**
     * Tool images
     */
    private Image maceImage;
    private Image tileImage;
    private Image coinImage;
    private Image startingPointImage;
    private Image endPointImage;

    private Image currentImage;

    /**
     * Canvas attributes
     */
    private GraphicsContext gc;
    private final int GRID_SIZE = 64;
    private int canvasWidth;
    private int canvasHeight;

    /**
     * Canvas coordinates
     */
    private int pressedX;
    private int pressedY;
    private int currentOffsetX = 0;
    private int currentOffsetY = 0;
    private int lastOffsetX = 0;
    private int lastOffsetY = 0;

    private boolean dragging = false;

    /**
     * Steps for undo and redo
     */
    private List<Step> steps;
    private int stepDiff = 0;


    @FXML
    protected void chooseTile() {
        enableTool(TILE_ENABLED);
    }

    @FXML
    protected void chooseCoin() {
        enableTool(COIN_ENABLED);
    }

    @FXML
    protected void chooseEnemy() {
        enableTool(ENEMY_ENABLED);
    }

    @FXML
    protected void chooseStartingPoint() {
        enableTool(STARTING_POINT_ENABLED);
    }

    @FXML
    protected void chooseEndPoint() {
        enableTool(END_POINT_ENABLED);
    }

    @FXML
    protected void chooseEraser() {
        enableTool(ERASER_ENABLED);
    }

    private void enableTool(char tool) {
        toolEnabled = tool;
        updateGui(tool);
    }

    @FXML
    protected void stepBackward() {
        if (steps.size() <= stepDiff) {
            System.out.println("Can't undo");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't undo", ButtonType.OK);
            alert.show();
        } else {
            stepDiff++;
            Step step = steps.get(steps.size() - stepDiff);
            editCell(step.getX(), step.getY(), step.getLastValue(), false);
        }
    }

    @FXML
    protected void stepForward() {
        if (stepDiff == 0) {
            System.out.println("Can't redo");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't redo", ButtonType.OK);
            alert.show();
        } else {
            stepDiff--;
            Step step = steps.get(steps.size() - stepDiff - 1);
            editCell(step.getX(), step.getY(), step.getCurrentValue(), false);
        }
    }

    @FXML
    protected void openLoadFile() {
        FileChooser fileChooser = createMapFileChooser("Load map");
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());

        if (file != null) {
            map = MapParser.getArrayFromFile(file);
            render(false);
        }
    }

    @FXML
    protected void openSaveFile() {
        try {
            validateMap();
            FileChooser fileChooser = createMapFileChooser("Save map");
            File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());

            if (file != null) {
                System.out.println(file);
                String content = getMapContent();
                saveFile(content, file);
            }
        } catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }

    private FileChooser createMapFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File("src/Resources/maps"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser;
    }

    private void validateMap() throws InvalidMapException {
        if (!startingPointExists || !endPointExists) {
            StringBuilder errors = new StringBuilder();
            if (!startingPointExists) {
                System.out.println("Starting point missing");
                errors.append("Starting point missing\n");
            }

            if (!endPointExists) {
                System.out.println("Finish point missing");
                errors.append("Finish point missing");
            }

            Alert alert = new Alert(Alert.AlertType.ERROR, errors.toString(), ButtonType.OK);
            alert.setHeaderText(null);
            alert.show();

            throw new InvalidMapException(errors.toString());
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
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGui(char tool) {
        playerButton.setBorder(null);
        tileButton.setBorder(null);
        coinButton.setBorder(null);
        enemyButton.setBorder(null);
        chestButton.setBorder(null);
        eraseButton.setBorder(null);

        if (tool == TILE_ENABLED) {
            currentImage = tileImage;
            tileButton.setBorder(new Border(new BorderStroke(Color.GREEN,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        } else if (tool == ENEMY_ENABLED) {
            currentImage = maceImage;
            enemyButton.setBorder(new Border(new BorderStroke(Color.GREEN,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        } else if (tool == COIN_ENABLED) {
            currentImage = coinImage;
        } else if (tool == STARTING_POINT_ENABLED) {
            currentImage = startingPointImage;
        } else if (tool == END_POINT_ENABLED) {
            currentImage = endPointImage;
        } else {
            currentImage = null;
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (dragging) {
            dragging = false;
        } else {
            int y = (int) (Math.floor((mouseEvent.getY() + currentOffsetY) / GRID_SIZE) + (map.length - (canvasHeight / GRID_SIZE)));
            int x = (int) (Math.floor((mouseEvent.getX() + currentOffsetX) / GRID_SIZE));

            editCell(x, y, toolEnabled, true);
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.isShiftDown()) {
            dragging = true;
            currentOffsetX = (int) (lastOffsetX + pressedX - mouseEvent.getX());
            currentOffsetY = (int) (lastOffsetY + pressedY - mouseEvent.getY());
            render(false);
        } else {
            mouseClicked(mouseEvent);
        }
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

        gc.setLineWidth(0.2);
        gc.setFill(Color.BLACK);

        map = new char[100][1000];
        render(true);
    }

    private void initializeSprites() {
        maceImage = new Image("/Resources/buttons/mace.png");
        coinImage = new Image("/Resources/buttons/coin.png");
        tileImage = new Image("/Resources/buttons/tile.png");
        startingPointImage = new Image("/Resources/buttons/player.png");
        endPointImage = new Image("/Resources/buttons/chest.png");
    }

    private void editCell(int x, int y, char tool, boolean addStep) {
        try {
            handleEditCellErrors(tool);
            updateGui(tool);

            gc.clearRect((x * GRID_SIZE) - currentOffsetX + 2, canvasHeight - ((map.length - y) * GRID_SIZE) - currentOffsetY + 2, 61, 61);

            if (addStep) {
                addStep(x, y, tool);
            }

            updateToolMonitoring(map[y][x], tool);
            map[y][x] = tool;

            if (tool != ERASER_ENABLED) {
                renderCell(x, y);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditCellErrors(char tool) throws Exception {
        if (tool == STARTING_POINT_ENABLED && startingPointExists) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Starting point already placed", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.show();
            throw new Exception("Starting point already placed");
        } else if (tool == END_POINT_ENABLED && endPointExists) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Finish point already placed", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.show();
            throw new Exception("Finish point already placed");
        }
    }

    private void updateToolMonitoring(char currentCellValue, char tool) {
        if (currentCellValue == STARTING_POINT_ENABLED) {
            startingPointExists = false;
        } else if (currentCellValue == END_POINT_ENABLED) {
            endPointExists = false;
        }

        if (tool == STARTING_POINT_ENABLED) {
            startingPointExists = true;
        } else if (tool == END_POINT_ENABLED) {
            endPointExists = true;
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

    private void render(boolean initialize) {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                updateGui(map[y][x]);
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

    @FXML
    public void openHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "- Choose a tool\n- Click in cell to draw enabled tool by clicking a button\n- Move mouse while holding shift to drag through map\n- All maps need one starting point (Player) and one finish point (Chest)", ButtonType.OK);
        alert.setHeaderText(null);
        alert.show();
    }
}