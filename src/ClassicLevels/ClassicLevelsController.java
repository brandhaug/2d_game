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
import javafx.stage.Stage;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassicLevelsController {

    @FXML
    private TableView<LevelColumn> standardLevelList, customLevelList;

    @FXML
    private Label errorLabel;

    @FXML
    private Button mainMenuButton;

    private SceneChanger sceneChanger;
    private short progress;
    private MapParser mapParser;

    private String[] standardLevels = new String[]{"1_Beginner", "2_Intermediate", "3_Hard", "4_Expert", "5_Legend"};


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

        addLevelsToList(standardLevelList, "standard");
        addLevelsToList(customLevelList, "custom");
    }

    /**
     * Add levels from list to tables
     * Add columns to table
     *
     * @param levelList
     * @param folderName
     */
    private void addLevelsToList(TableView<LevelColumn> levelList, String folderName) {

        final String path = "Resources/maps/classic/" + folderName;
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if (jarFile.isFile()) {
            try {
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String filePath = entries.nextElement().getName();
                    if (filePath.startsWith(path + "/")) { //filter according to the path
                        String separator = "/";
                        int pos = filePath.lastIndexOf(separator);
                        String name = filePath.substring(pos + separator.length());

                        if (name.length() > 0) {
                            String status = getStatusOnName(name, folderName);
                            LevelColumn levelColumn = new LevelColumn(name, status);
                            levelList.getItems().add(levelColumn);
                        }
                    }
                }

                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else { // Run with IDE
            final URL url = ClassicLevelsController.class.getResource("/" + path);
            if (url != null) {
                try {
                    final File levelFiles = new File(url.toURI());
                    for (File levelFile : levelFiles.listFiles()) {
                        String status = getStatusOnFile(levelFile, folderName);
                        LevelColumn levelColumn = new LevelColumn(levelFile.getName(), status);
                        levelList.getItems().add(levelColumn);
                    }
                } catch (URISyntaxException ex) {
                    // never happens
                }
            }
        }

        addColumnsToList(levelList);
    }

    /**
     * Set locked/unlocked on levels, based on progress
     *
     * @param levelFile
     * @param folderName
     * @return
     */
    private String getStatusOnFile(File levelFile, String folderName) {
        int level;

        if (folderName.equals("standard")) {
            level = mapParser.getValueFromFileHeader(levelFile, "level");
        } else {
            level = 0;
        }

        if (level > progress) {
            return "Locked";
        } else {
            return "Unlocked";
        }
    }

    /**
     * Set locked/unlocked on levels, based on progress
     *
     * @param name
     * @param folderName
     * @return
     */
    private String getStatusOnName(String name, String folderName) {
        int level;

        if (folderName.equals("standard")) {
            level = Integer.parseInt(String.valueOf(name.charAt(0)));
        } else {
            level = 0;
        }

        if (level > progress) {
            return "Locked";
        } else {
            return "Unlocked";
        }
    }

    /**
     * Adds columns and sets width for list
     *
     * @param levelList
     */
    private void addColumnsToList(TableView<LevelColumn> levelList) {
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
     */
    public void standardTableClicked() {
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
     */
    public void customTableClicked() {
        if (customLevelList.getSelectionModel().getSelectedItem() != null &&
                customLevelList.getSelectionModel().getSelectedItem().getStatus().equals("Locked")) {
            errorLabel.setText("Level is not unlocked");
        } else if (customLevelList.getSelectionModel().getSelectedItem() != null) {
            openGameLevel("classic/custom/" + customLevelList.getSelectionModel().getSelectedItem().getName());
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
}