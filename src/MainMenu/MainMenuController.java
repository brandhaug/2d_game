package MainMenu;

import Game.GameController;
import Highscores.HighScoreHandler;
import SceneChanger.SceneChanger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class MainMenuController {

    //    @FXML
//    private Pane mapsPane;
//    @FXML
//    private ImageView map1;
//    @FXML
//    private ImageView map2;
//    @FXML
//    private ImageView map3;
//    @FXML
//    private ImageView map4;
    @FXML
    private Button infoButton, exitButton, highscoresButton, playButtonSurvival,
            playButtonLevel, soundButton, musicButton, BULLET_A, BULLET_B, BULLET_C,
            createLevelButton, confirm, decline, killCoin1, killCoin2, killCoin3,
            exitBulletBuyPane, exitChooseBulletPane;
    @FXML
    private Text bullet1Price, bullet2Price;
    @FXML
    private Text bullet3Price;
    @FXML
    private Text bullet1Owned;
    @FXML
    private Text bullet2Owned;
    @FXML
    private Text bullet3Owned;
    @FXML
    private Label killCoinInfo;
    @FXML
    private Pane chooseBulletPane;
    @FXML
    private Pane buyBulletPane;
    @FXML
    private Pane buyBulletPaneConfirm;

    private SceneChanger sceneChanger;
    private Preferences preferences = Preferences.userRoot();
    private Clip musicClip;
    private boolean initialized = false;
    private boolean bullet1Available;
    private boolean bullet2Available;
    private boolean bullet3Available;
    private String bulletPrice;
    public static String selectedBullet;
    private HighScoreHandler highScoreHandler;

    public MainMenuController() throws IOException {
        highScoreHandler = new HighScoreHandler();
    }

    @FXML
    protected void exit() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void toggleSound() {
        boolean soundOn = preferences.getBoolean("sound", true);

        if (initialized) {
            preferences.putBoolean("sound", !soundOn);
            soundOn = !soundOn;
        }

        if (soundOn) {
            soundButton.setStyle("-fx-graphic: 'Resources/buttons/sound_on.png'");
        } else {
            soundButton.setStyle("-fx-graphic: 'Resources/buttons/sound_off.png'");
        }
    }

    @FXML
    protected void toggleMusic() {
        boolean musicOn = preferences.getBoolean("music", true);

        if (initialized) {
            preferences.putBoolean("music", !musicOn);
            musicOn = !musicOn;
        }

        if (musicOn) {
            musicButton.setStyle("-fx-graphic: 'Resources/buttons/music_on.png'");
            musicClip.start();
        } else {
            musicButton.setStyle("-fx-graphic: 'Resources/buttons/music_off.png'");
            musicClip.stop();
        }
    }

    @FXML
    protected void openGameLevel(ActionEvent event) {
        musicClip.stop();
//        GameController.setMapName("game");
        sceneChanger.changeScene(event, "../ClassicLevels/ClassicLevels.fxml", true);
    }

    @FXML
    protected void openGameSurvival(ActionEvent event) {
        musicClip.stop();
        GameController.setMapName("survivalfolder/survival");
        sceneChanger.changeScene(event, "../Game/Game.fxml", true);
    }

    @FXML
    protected void bulletSelected(ActionEvent event) {
        int points = getKillCoins();

        if (BULLET_A.isFocused()) {
            selectedBullet = BULLET_A.getId();
            if (bullet1Available) {
                openGameSurvival(event);
            } else if (points >= Integer.parseInt(bullet1Price.getText())) {
                buyBulletPane.setVisible(true);
                bulletPrice = bullet1Price.getText();
            }
        }
        if (BULLET_B.isFocused()) {
            selectedBullet = BULLET_B.getId();
            if (bullet2Available) {
                openGameSurvival(event);
            } else if (points >= Integer.parseInt(bullet2Price.getText())) {
                buyBulletPane.setVisible(true);
                bulletPrice = bullet2Price.getText();
            }
        }
        if (BULLET_C.isFocused()) {
            selectedBullet = BULLET_C.getId();
            if (bullet3Available) {
                openGameSurvival(event);
            } else if (points >= Integer.parseInt(bullet3Price.getText())) {
                buyBulletPane.setVisible(true);
                bulletPrice = bullet3Price.getText();
            }
        }

        if (exitChooseBulletPane.isFocused()) chooseBulletPane.setVisible(false);
    }

    private ArrayList<String> getSurvivalFileContent() {
        highScoreHandler.decryptFile(highScoreHandler.getSurvivalPath());
        ArrayList<String> arrayList = highScoreHandler.getArrayListFromFile(highScoreHandler.getSurvivalPath());
        highScoreHandler.encryptFile(highScoreHandler.getSurvivalPath());
        return arrayList;
    }

    private int getKillCoins() {
        int points = 0;
        try {
            points = Integer.parseInt(getSurvivalFileContent().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }

    @FXML
    protected void bulletPurchase() {
        int points = getKillCoins();

        if (confirm.isFocused()) {
            try {
                ArrayList<String> survivalFileContent = getSurvivalFileContent();
                survivalFileContent.set(0, Integer.toString(points - Integer.parseInt(bulletPrice)));
                Files.write(highScoreHandler.getSurvivalPath(), survivalFileContent, StandardCharsets.ISO_8859_1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateAvailableBullets();
            buyBulletPane.setVisible(false);
        }

        if (exitBulletBuyPane.isFocused() || decline.isFocused()) buyBulletPane.setVisible(false);
    }

    private void updateAvailableBullets() {
        try {
            int position = 0;

            BufferedReader reader = new BufferedReader(new FileReader("survivalInfo.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (line.contains(selectedBullet)) {
                    ArrayList<String> survivalFileContent = getSurvivalFileContent();
                    survivalFileContent.set(position, selectedBullet + "=true");
                    Files.write(highScoreHandler.getSurvivalPath(), survivalFileContent, StandardCharsets.ISO_8859_1);
                    break;
                }
                position++;
                line = reader.readLine();
            }

            chooseBullet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void chooseBullet() {
        try {
            highScoreHandler.decryptFile(highScoreHandler.getSurvivalPath());
            BufferedReader reader = new BufferedReader(new FileReader("survivalInfo.txt"));
            String line = reader.readLine();

            line = reader.readLine();
            if (line.contains("true")) {
                BULLET_A.setStyle("-fx-graphic: 'Resources/buttons/bullet_A_Available.png'");
                bullet1Available = true;
                bullet1Price.setVisible(false);
                killCoin1.setVisible(false);
                bullet1Owned.setVisible(true);
            }

            line = reader.readLine();
            if (line.contains("true")) {
                BULLET_B.setStyle("-fx-graphic: 'Resources/buttons/bullet_B_Available.png'");
                bullet2Available = true;
                bullet2Price.setVisible(false);
                killCoin2.setVisible(false);
                bullet2Owned.setVisible(true);
            }

            line = reader.readLine();
            if (line.contains("true")) {
                BULLET_C.setStyle("-fx-graphic: 'Resources/buttons/bullet_C_Available.png'");
                bullet3Available = true;
                bullet3Price.setVisible(false);
                killCoin3.setVisible(false);
                bullet3Owned.setVisible(true);
            }
            highScoreHandler.encryptFile(highScoreHandler.getSurvivalPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        chooseBulletPane.setVisible(true);
    }

    @FXML
    protected void openInfo(ActionEvent event) {
        musicClip.stop();
        sceneChanger.changeScene(event, "../Info/Info.fxml", true);
    }

    @FXML
    protected void openCreateLevel(ActionEvent event) {
        musicClip.stop();
        sceneChanger.changeScene(event, "../CreateLevel/CreateLevel.fxml", false);
    }

    @FXML
    protected void openHighscores(ActionEvent event) {
        musicClip.stop();
        sceneChanger.changeScene(event, "../Highscores/Highscores.fxml", true);
    }

    @FXML
    public void initialize() {
        sceneChanger = new SceneChanger();
        killCoinInfo.setText("KillCoins: " + getKillCoins());

        // TODO: Sett styles i FXML eller i stylesheet
//        mapsPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
//        mapsPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        try {
            initializeMusic();
            toggleMusic();
            toggleSound();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }

//        map1.setOnMouseEntered(event -> map1.setImage(new Image("Resources/background/map1.gif")));
//        map1.setOnMouseExited(event -> map1.setImage(new Image("Resources/background/map1_still_image.png")));
//        map2.setOnMouseEntered(event -> map2.setImage(new Image("Resources/background/map1.gif")));
//        map2.setOnMouseExited(event -> map2.setImage(new Image("Resources/background/map1_still_image.png")));
//        map3.setOnMouseEntered(event -> map3.setImage(new Image("Resources/background/map1.gif")));
//        map3.setOnMouseExited(event -> map3.setImage(new Image("Resources/background/map1_still_image.png")));
//        map4.setOnMouseEntered(event -> map4.setImage(new Image("Resources/background/map1.gif")));
//        map4.setOnMouseExited(event -> map4.setImage(new Image("Resources/background/map1_still_image.png")));
        initialized = true;
    }


    /**
     * Create an AudioInputStream from a given sound file
     * Acquire music format and create a DataLine.Infoobject
     * Obtain the Clip
     * Open the AudioInputStream and start playing
     *
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public void initializeMusic() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream musicStream = AudioSystem.getAudioInputStream(new File(getClass().getResource("/Resources/music/main_song.wav").getPath()));
        musicClip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, musicStream.getFormat()));
        musicClip.open(musicStream);
        musicClip.loop(10);
        musicClip.start();
    }
}
