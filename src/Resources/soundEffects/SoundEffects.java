package Resources.soundEffects;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public enum SoundEffects {

    COIN("/Resources/sounds/coinCollect.wav"),
    HIT("/Resources/sounds/hit.wav"),
    GAMEOVER("/Resources/sounds/gameOver.wav"),
    RUN("/Resources/sounds/steps.wav"),
    JUMP("/Resources/sounds/jump.wav"),
    LANDING("/Resources/sounds/landing.wav"),
    ENEMY_DEATH("/Resources/sounds/enemyDeath.wav");

    public static boolean mute;
    private Clip effectClip;
    double gain = 0.25;
    float db = (float) (Math.log(gain)/Math.log(10.0) *20.0);

    SoundEffects(String soundFileName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(soundFileName);
            InputStream bufferedInputStream = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            effectClip = AudioSystem.getClip();
            effectClip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if(!mute) {
            effectClip.stop();
            effectClip.setFramePosition(0);
            effectClip.start();
        }
    }

    public void playLoop() {
        if (!mute) {
            if (!effectClip.isRunning()) {
                FloatControl volume = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(db);
                effectClip.stop();
                effectClip.setFramePosition(0);
                effectClip.start();
            }
        }
    }

    public void stopLoop(){
        effectClip.stop();
    }
}
