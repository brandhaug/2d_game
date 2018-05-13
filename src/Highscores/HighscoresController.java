package Highscores;

import SceneChanger.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class HighscoresController {

    @FXML
    private Button mainMenuButton;
    @FXML
    private TreeView<String> treeView;

    private SceneChanger sceneChanger;
    private FileHandler fileHandler;

    private Image firstPlace = new Image("/Resources/buttons/gold.png");
    private Image secondPlace = new Image("/Resources/buttons/silver.png");
    private Image thirdPlace = new Image("/Resources/buttons/bronze.png");

    /**
     * Changes to the main menu scene
     * @param event the Action Event
     */
    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "/MainMenu/MainMenu.fxml", true);
    }

    /**
     * Reload the high score scene, by changing the scene to itself.
     */
    @FXML
    private void reloadHighScoresScene() {
        sceneChanger.changeScene((Stage) mainMenuButton.getScene().getWindow(), "/Highscores/Highscores.fxml", true);
    }

    /**
     * Sets the tree view used in the list representing the high scores.
     */
    private void setTreeView() {
        fileHandler.decryptFile(fileHandler.getHighScorePath());
        ArrayList<String> list = fileHandler.getArrayListFromFile(fileHandler.getHighScorePath());

        // Set root
        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(true);

        boolean survival = false;
        // Set branches
        TreeItem<String> map = null;
        int placement = 1;
        for (String item : list) {
            if (item.contains("map=")) {
                placement = 1;
                int position = item.indexOf("=");
                String mapName = item.substring(position + 1, item.length());
                map = makeBranch(mapName, root);
            } else {
                String info;
                if (map != null) {
                    if (map.getValue().matches(".*survival.*")) survival = true;
                    int time = fileHandler.getTimeFromLine(item);
                    int objectAmount = fileHandler.getObjectAmountFromLine(item);
                    if (survival) {
                        info = "Time: " + time + "\n" + "Kills: " + objectAmount;
                    } else {
                        info = "Time: " + time + "\n" + "Coins: " + objectAmount;
                    }
                    survival = false;
                    makeBranch(info, map, placement);
                    placement++;
                }
            }
        }

        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> System.out.println(newValue.getValue()));
        fileHandler.encryptFile(fileHandler.getHighScorePath());
    }

    /**
     * Makes a new tre branch
     * @param name the branch name
     * @param parent the parent node
     * @return TreeItem<String>
     */
    private TreeItem<String> makeBranch(String name, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(name);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    /**
     * Makes a new tree branch
     * @param name the tree name
     * @param parent the parent item
     * @param placement placement in tree
     */
    private void makeBranch(String name, TreeItem<String> parent, int placement) {
        ImageView imageView = null;
        switch (placement) {
            case 1:
                imageView = new ImageView(firstPlace);
                break;
            case 2:
                imageView = new ImageView(secondPlace);
                break;
            case 3:
                imageView = new ImageView(thirdPlace);
                break;
        }

        TreeItem<String> item;
        if (imageView != null) {
            imageView.setFitWidth(50);
            imageView.setFitHeight(47);
            item = new TreeItem<>(name, imageView);
        } else {
            item = new TreeItem<>(name);
        }

        item.setExpanded(true);
        parent.getChildren().add(item);
    }

    /**
     * Initializes a FileHandler object.
     * Initializes a new SceneChanger.
     * Sets the tree view.
     */
    @FXML
    public void initialize() {
        fileHandler = FileHandler.getInstance();
        sceneChanger = new SceneChanger();
        setTreeView();
    }

    /**
     * Opens a alert dialog, and resets all high scores if user confirms.
     */
    @FXML
    public void resetHighScores() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("High scores");
        alert.setHeaderText("You are trying to reset all high scores.\nThis can not be undone!");
        alert.setContentText("Are you sure?");

        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            fileHandler.resetHighScores();
            reloadHighScoresScene();
        }
    }
}
