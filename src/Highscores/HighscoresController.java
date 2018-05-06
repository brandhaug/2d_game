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
    private HighScoreHandler highScoreHandler;

    private Image firstPlace = new Image("/Resources/buttons/gold.png");
    private Image secondPlace = new Image("/Resources/buttons/silver.png");
    private Image thirdPlace = new Image("/Resources/buttons/bronze.png");

    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    @FXML
    private void reloadHighScoresScene() {
        sceneChanger.changeScene((Stage) mainMenuButton.getScene().getWindow(), "../Highscores/Highscores.fxml", true);
    }

    private void setTreeView() {
        highScoreHandler.decryptFile(highScoreHandler.getFilePath());
        ArrayList<String> list = highScoreHandler.getArrayListFromFile(highScoreHandler.getFilePath());

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
                    int time = highScoreHandler.getTimeFromLine(item);
                    int objectAmount = highScoreHandler.getObjectAmountFromLine(item);
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
        highScoreHandler.encryptFile(highScoreHandler.getFilePath());
    }

    private TreeItem<String> makeBranch(String name, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(name);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

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

    @FXML
    public void initialize() {
        highScoreHandler = new HighScoreHandler();
        sceneChanger = new SceneChanger();
        setTreeView();
    }

    @FXML
    public void resetHighScores() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("High scores");
        alert.setHeaderText("You are trying to reset all high scores.\nThis can not be undone!");
        alert.setContentText("Are you sure?");

        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            highScoreHandler.resetHighScores();
            reloadHighScoresScene();
        }
    }
}
