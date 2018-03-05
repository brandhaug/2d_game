package Resources.soundEffects;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;

public enum SoundEffects {

    COIN("./Resources/sounds/coinCollect.wav"),
    HIT("./Resources/sounds/hit.wav"),
    GAMEOVER("./Resources/sounds/gameOver.wav"),
    RUN("./Resources/sounds/steps.wav"),
    JUMP("./Resources/sounds/jump.wav"),
    LANDING("./Resources/sounds/landing.wav");

    public static boolean mute;
    private Clip effectClip;
    double gain = 0.25;
    float db = (float) (Math.log(gain)/Math.log(10.0) *20.0);

    SoundEffects(String soundFileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            effectClip = AudioSystem.getClip();
            effectClip.open(audioInputStream);
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
