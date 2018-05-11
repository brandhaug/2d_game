package ClassicLevels;

import CreateLevel.MapParser;
import Game.GameController;
import Highscores.FileHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import SceneChanger.SceneChanger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ClassicLevelsController {

    @FXML
    private TableView<LevelColumn> standardLevelList, customLevelList;

    @FXML
    private Label errorLabel;

    @FXML
    private Button mainMenuButton;

    private SceneChanger sceneChanger;
    private int progress;


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
     * @param mapName
     */
    @FXML
    private void openGameLevel(String mapName) {
        GameController.setMapName(mapName);
        sceneChanger.changeScene((Stage) standardLevelList.getScene().getWindow(), "../Game/Game.fxml", true);
    }

    /**
     * Change scene to Main Menu
     * @param event
     */
    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    /**
     * Initializes ClassicLevels
     * Get progress
     * Add standard and custom levels to list
     */
    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
        FileHandler fileHandler = FileHandler.getInstance();
        progress = fileHandler.getProgress();
        if (progress == -1) {
            errorLabel.setText("Invalid progress file. Deleting 'progress.txt' may help, but all your progress will be lost");
            progress = 1;
        }

        addLevelsToList(standardLevelList, "standard");
        addLevelsToList(customLevelList, "custom");
    }

    /**
     * Add levels from list to tables
     * Set locked/unlocked on levels, based on progress
     * Add columns to table
     * @param levelList
     * @param folderName
     */
    private void addLevelsToList(TableView<LevelColumn> levelList, String folderName) {
        File levelFolder = new File("src/Resources/maps/classic/" + folderName);
        File[] levelFiles = levelFolder.listFiles();

        if (levelFiles != null && levelFiles.length != 0) {
            String status;
            int level;

            for (File levelFile : levelFiles) {
                if (folderName.equals("standard")) {
                    level = MapParser.getValueFromFile(levelFile, "level");
                } else {
                    level = 0;
                }

                if (level > progress) {
                    status = "Locked";
                } else {
                    status = "Unlocked";
                }

                LevelColumn levelColumn = new LevelColumn(levelFile.getName(), status);
                levelList.getItems().add(levelColumn);
            }

        }

        TableColumn<LevelColumn, String> firstCol = new TableColumn<>("Level");
        firstCol.setMinWidth(200);
        firstCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<LevelColumn, String> secondCol = new TableColumn<>("Status");
        secondCol.setMinWidth(95);
        secondCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        levelList.getColumns().addAll(firstCol, secondCol);
    }

    /**
     * Opens game with selected map
     * If selected map is not unlocked - Sends error message to user
     * @param event
     */
    public void standardTableClicked(MouseEvent event) {
        if (standardLevelList.getSelectionModel().getSelectedItem() != null &&
                standardLevelList.getSelectionModel().getSelectedItem().getStatus().equals("Locked")) {
            errorLabel.setText("Level is not unlocked");
        } else if (standardLevelList.getSelectionModel().getSelectedItem() != null) {
            openGameLevel("classic/standard/" + standardLevelList.getSelectionModel().getSelectedItem().getName());
        }
    }


    /**
     * Opens game with selected map
     * If selected map is not unlocked - Sends error message to user
     * @param event
     */
    public void customTableClicked(MouseEvent event) {
        if (customLevelList.getSelectionModel().getSelectedItem() != null &&
                customLevelList.getSelectionModel().getSelectedItem().getStatus().equals("Locked")) {
            errorLabel.setText("Level is not unlocked");
        } else if (customLevelList.getSelectionModel().getSelectedItem() != null) {
            openGameLevel("classic/custom/" + customLevelList.getSelectionModel().getSelectedItem().getName());
        }
    }
}