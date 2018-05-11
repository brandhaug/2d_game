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
    private static Label errorLabel;

    @FXML
    private Button mainMenuButton;

    private SceneChanger sceneChanger;
    private FileHandler fileHandler;
    private int progress;


    @FXML
    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void openGameLevel(String mapName) {
        GameController.setMapName(mapName);
        sceneChanger.changeScene((Stage) standardLevelList.getScene().getWindow(), "../Game/Game.fxml", true);
    }

    public static void setErrorLabel(String error) {
        errorLabel.setText(error);
    }

    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
        fileHandler = FileHandler.getInstance();
        progress = fileHandler.getProgress();

        addLevelsToList(standardLevelList, "standard");
        addLevelsToList(customLevelList, "custom");
    }

    private void addLevelsToList(TableView<LevelColumn> levelList, String folderName) {
        File levelFolder = new File("src/Resources/maps/classic/" + folderName);
        File[] levelFiles = levelFolder.listFiles();

        if (levelFiles == null || levelFiles.length == 0) {

        } else {
            String status;
            int level;

            for (int i = 0; i < levelFiles.length; i++) {
                if (folderName.equals("standard")) {
                    level = MapParser.getValueFromFile(levelFiles[i], "level");
                } else {
                    level = 0;
                }

                if (level > progress) {
                    status = "Locked";
                } else {
                    status = "Unlocked";
                }

                LevelColumn levelColumn = new LevelColumn(levelFiles[i].getName(), status);
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

    private int getProgress() throws IOException {
        Path path = Paths.get("progress.txt");
        List<String> lines = Files.readAllLines(path, StandardCharsets.ISO_8859_1);

        try {
            return Integer.parseInt(lines.get(0));
        } catch (Exception e){
            errorLabel.setText("Invalid progress file - Progress set to 1");
        }
        return 1;
    }

    public void standardTableClicked(MouseEvent event) {
        if (standardLevelList.getSelectionModel().getSelectedItem().getStatus().equals("Locked")) {
            errorLabel.setText("Level is not unlocked");
        } else {
            openGameLevel("classic/standard/" + standardLevelList.getSelectionModel().getSelectedItem().getName());
        }
    }

    public void customTableClicked(MouseEvent event) {
        if (customLevelList.getSelectionModel().getSelectedItem().getStatus().equals("Locked")) {
            errorLabel.setText("Level is not unlocked");
        } else {
            openGameLevel("classic/custom/" + customLevelList.getSelectionModel().getSelectedItem().getName());
        }
    }
}