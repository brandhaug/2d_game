package Game;

import Game.GameObjects.Player;
import Game.Levels.Level;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.Preferences;

public class GameController {

    public final static int CANVAS_WIDTH = 1280;
    public final static int CANVAS_HEIGHT = 720;
    public final static int BUTTON_SIZE = 64;
    public final static int TILE_SIZE = 128;
    public final static int MARGIN = 32;

    private Level level;
    private Player player;
    private CollisionHandler collisionHandler;
    private Preferences preferences = Preferences.userRoot();

    private Image background;
    private GraphicsContext gc;

    private boolean gamePaused = false;
    private boolean initialized = false;

    @FXML
    private Canvas canvas;
    @FXML
    private Button pauseButton;
    @FXML
    private Button soundButton;
    @FXML
    private Button musicButton;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Pane pauseInfoPane;
    @FXML
    private Pane pauseSettingsPane;
    @FXML
    private Text pauseText;

    @FXML
    protected void openMainMenu() {
        try {
            Stage stage = (Stage) mainMenuButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../MainMenu/MainMenu.fxml"));
            root.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pause() {
        if (gamePaused) {
            pauseButton.setStyle("-fx-graphic: 'Resources/buttons/pause.png'");
        } else {
            pauseButton.setStyle("-fx-graphic: 'Resources/buttons/play.png'");
        }
        soundButton.setVisible(!gamePaused);
        musicButton.setVisible(!gamePaused);
        mainMenuButton.setVisible(!gamePaused);
        pauseInfoPane.setVisible(!gamePaused);
        pauseText.setVisible(!gamePaused);
        pauseSettingsPane.setVisible(!gamePaused);
        gamePaused = !gamePaused;
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
        } else {
            musicButton.setStyle("-fx-graphic: 'Resources/buttons/music_off.png'");
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        if (!gamePaused) {
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                player.setVelocityX(player.getVelocityX() + 4);
            }
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                player.setVelocityX(player.getVelocityX() - 4);
            }
            if ((code == KeyCode.UP || code == KeyCode.W) && player.getVelocityY() == 0) {
                player.setVelocityY(-35);
            }
        }
    }

    @FXML
    private void handleKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();

        if (!gamePaused) {
            if (code == KeyCode.RIGHT || code == KeyCode.D) player.setVelocityX(0);
            if (code == KeyCode.LEFT || code == KeyCode.A) player.setVelocityX(0);
//            if (code == KeyCode.UP || code == KeyCode.W) player.setVelocityY(0);
        }
    }


    @FXML
    public void initialize() {
        initializeGUI();
        initializeBackground();

        gc = canvas.getGraphicsContext2D();

        try {
            level = new Level("beginner");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        player = new Player(200, 400);
        collisionHandler = new CollisionHandler(player, level);

        final long startNanoTime = System.nanoTime();

        initialized = true;

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gameLoop(startNanoTime, currentNanoTime);
            }
        }.start();
    }

    private void gameLoop(long startNanoTime, long currentNanoTime) {
        double time = (currentNanoTime - startNanoTime) / 1000000000.0;

        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        drawBackground(gc);
        player.handleSpriteState();
        collisionHandler.tick();
        level.tick(gc, player.getVelocityX(), time);
        player.tick(gc);
    }

    private void initializeGUI() {
        //TODO: Move to FXML
        pauseInfoPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
        pauseInfoPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        pauseSettingsPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
    }

    private void initializeBackground() {
        try {
            BufferedImage bufferedBackground = ImageIO.read(new File(getClass().getResource("/Resources/background/background.png").getPath()));
            background = SwingFXUtils.toFXImage(bufferedBackground, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawBackground(GraphicsContext gc) {
        int tempX = player.getX();
        while (tempX > CANVAS_WIDTH + player.getStartPosition()) {
            tempX -= CANVAS_WIDTH;
        }

        gc.drawImage(background, player.getStartPosition() - tempX, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.drawImage(background, CANVAS_WIDTH + player.getStartPosition() - tempX, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }
}