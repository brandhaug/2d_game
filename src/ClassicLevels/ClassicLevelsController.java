package ClassicLevels;

import CreateLevel.MapParser;
import Game.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import SceneChanger.SceneChanger;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;

public class ClassicLevelsController {

    @FXML
    private TableView<LevelColumn> standardLevelList, customLevelList;

    @FXML
    private Label errorLabel;

    private SceneChanger sceneChanger;
    private int progress;


    @FXML
    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    private void openGameLevel(String mapName) {
        GameController.setMapName(mapName);
        sceneChanger.changeScene((Stage) errorLabel.getScene().getWindow(), "../Game/Game.fxml", true);
    }

    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();

        try {
            progress = getProgress();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        BufferedReader br = new BufferedReader(new FileReader("progress.txt"));
        String progressString = br.readLine();
        br.close();

        if (progressString.length() == 0) {
            System.out.println("No progress found in file");
        }

        int progressInt = 1;

        try {
            progressInt = Integer.parseInt(progressString);
        } catch (Exception e){
            errorLabel.setText("Invalid progress file - Progress set to 1");
        }
        return progressInt;
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