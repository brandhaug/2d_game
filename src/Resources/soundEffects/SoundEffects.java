package Resources.soundEffects;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;

public enum SoundEffects {

    COIN("./Resources/music/coinCollect.wav");

    private Clip effectClip;
    private Preferences preferences = Preferences.userRoot();

    SoundEffects(String soundFileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            effectClip = AudioSystem.getClip();
            effectClip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
            effectClip.stop();
            effectClip.setFramePosition(0);
            effectClip.start();
    }

}
