package Highscores;

import SceneChanger.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class HighscoresController {

    @FXML
    private Button mainMenuButton;
    @FXML
    private TreeView<String> treeView;

    private SceneChanger sceneChanger;
    private HighscoreHandler highscoreHandler;

    private Image firstPlace = new Image("/Resources/buttons/gold.png");
    private Image secondPlace = new Image("/Resources/buttons/silver.png");
    private Image thirdPlace = new Image("/Resources/buttons/bronze.png");

    @FXML
    protected void openMainMenu(ActionEvent event) {
        sceneChanger.changeScene(event, "../MainMenu/MainMenu.fxml", true);
    }

    private void setTreeView() {
        ArrayList<String> list = highscoreHandler.getArrayListFromFile();

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
                    if(map.getValue().matches(".*survival.*")) survival = true;
                    int time = highscoreHandler.getTimeFromLine(item);
                    int objectAmount = highscoreHandler.getObjectAmountFromLine(item);
                    if (survival) {
                        info = "Time: " + time + "\n" + "Kills: " + objectAmount;
                    }else {
                    info = "Time: " + time + "\n" + "Coins: " + objectAmount;
                }
                    survival = false;
                    makeBranch(info, map, placement);
                    placement++;
                }
            }
        }

        // Map 1
        /*TreeItem<String> map1 = makeBranch("map=Game", root);
        makeBranch("time=10,coins=20", map1);
        makeBranch("time=10,coins=20", map1);
        makeBranch("time=10,coins=20", map1);

        // Map 1
        TreeItem<String> map2 = makeBranch("map=Game", root);
        makeBranch("time=10,coins=20", map2);
        makeBranch("time=10,coins=20", map2);
        makeBranch("time=10,coins=20", map2);*/


        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            System.out.println(newValue.getValue());
        });
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
        highscoreHandler = new HighscoreHandler();
        sceneChanger = new SceneChanger();
        setTreeView();
    }

}
