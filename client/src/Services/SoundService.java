package Services;

import Constants.Sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundService {

    public void play(Sound sound) {
        String resourcePath = sound.getResourcePath();
        try (InputStream raw = getClass().getResourceAsStream(resourcePath)) {
            if (raw == null) {
                System.err.println("Sound not found: " + resourcePath);
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(new BufferedInputStream(raw));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            System.err.println("Could not play sound " + sound + ": " + e.getMessage());
        }
    }
}


