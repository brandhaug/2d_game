package ClassicLevels;

import CreateLevel.MapParser;
import Game.GameController;
import Highscores.FileHandler;
import Jar.JarUtil;
import SceneChanger.SceneChanger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ClassicLevelsController {

    @FXML
    private TableView<LevelColumn> standardLevelList;

    @FXML
    private Label errorLabel;

    @FXML
    private Button mainMenuButton;

    private SceneChanger sceneChanger;
    private short progress;
    private MapParser mapParser;

    private String[] standardLevels = new String[]{"1_Beginner.txt", "2_Intermediate.txt", "3_Hard.txt", "4_Expert.txt", "5_Legend.txt"};


    /**
     * Closes application
     */
    @FXML
    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Set map name and change scene to Game
     *
     * @param mapName
     */
    @FXML
    private void openGameLevel(String mapName) {
        GameController.setMapName(mapName);
        sceneChanger.changeScene((Stage) standardLevelList.getScene().getWindow(), "/Game/Game.fxml", true);
    }

    /**
     * Change scene to Main Menu
     *
     * @param event
     */
    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "/MainMenu/MainMenu.fxml", true);
    }

    /**
     * Initializes ClassicLevels
     * Get progress
     * Add standard and custom levels to list
     */
    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
        mapParser = new MapParser();
        FileHandler fileHandler = FileHandler.getInstance();
        progress = fileHandler.getProgress();
        if (progress == -1) {
            errorLabel.setText("Invalid progress file. Deleting 'progress.txt' may help, but all your progress will be lost");
            progress = 1;
        }

        addStandardLevelsToList();
    }

    /**
     * Add levels from list to tables
     * Add columns to table
     */
    private void addStandardLevelsToList() {
        final String path = "/Resources/maps/classic/standard";

        for (String level : standardLevels) {
            String status = getStatusOnName(level);
            LevelColumn levelColumn = new LevelColumn(level, status);
            standardLevelList.getItems().add(levelColumn);
        }


        addColumnsToList();
    }

    /**
     * Set locked/unlocked on levels, based on progress
     *
     * @param name
     * @return
     */
    private String getStatusOnName(String name) {
        int level = Integer.parseInt(String.valueOf(name.charAt(0)));

        if (level > progress) {
            return "Locked";
        } else {
            return "Unlocked";
        }
    }

    /**
     * Adds columns and sets width for list
     */
    private void addColumnsToList() {
        TableColumn<LevelColumn, String> firstCol = new TableColumn<>("Level");
        firstCol.setMinWidth(200);
        firstCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<LevelColumn, String> secondCol = new TableColumn<>("Status");
        secondCol.setMinWidth(95);
        secondCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        standardLevelList.getColumns().addAll(firstCol, secondCol);
    }

    /**
     * Opens game with selected map
     * If selected map is not unlocked - Sends error message to user
     */
    public void standardTableClicked() {
        if (standardLevelList.getSelectionModel().getSelectedItem() != null &&
                standardLevelList.getSelectionModel().getSelectedItem().getStatus().equals("Locked")) {
            errorLabel.setText("Level is not unlocked");
        } else if (standardLevelList.getSelectionModel().getSelectedItem() != null) {
            String path = "/Resources/maps/classic/standard/";
            openGameLevel(path + standardLevelList.getSelectionModel().getSelectedItem().getName());
        }
    }

    /**
     * Get progress
     *
     * @return progress
     */
    public short getProgress() {
        return progress;
    }

    /**
     * Get Standard level list
     *
     * @return standard level list
     */
    public TableView<LevelColumn> getStandardLevelList() {
        return standardLevelList;
    }

    public void loadCustomLevel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load custom map");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(standardLevelList.getScene().getWindow());

        if (file != null) {
            MapParser mapParser = new MapParser();
            boolean validMap = mapParser.validateInputStreamMap(file.getAbsolutePath());

            if (!validMap) {
                errorLabel.setText("Invalid map");
            } else {
                openGameLevel(file.getAbsolutePath());
            }
        }
    }
}