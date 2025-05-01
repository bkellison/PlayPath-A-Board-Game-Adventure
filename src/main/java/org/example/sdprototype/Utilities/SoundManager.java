package org.example.sdprototype.Utilities;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages sound effects for the game using standard Java sound API
 */
public class SoundManager {
    private static final Map<String, Clip> clipCache = new HashMap<>();
    private static boolean soundEnabled = true;

    /**
     * Plays a sound from the resources folder
     * @param soundPath The path to the sound file (e.g., "/org/example/sdprototype/sounds/JumpSound.wav")
     */
    public static void playSound(String soundPath) {
        if (!soundEnabled) return;

        try {
            Clip clip = getClip(soundPath);
            if (clip != null) {
                // Reset to beginning and play
                clip.setFramePosition(0);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    /**
     * Gets a sound clip from the cache or loads it if not present
     */
    private static Clip getClip(String soundPath) {
        if (!clipCache.containsKey(soundPath)) {
            try {
                InputStream is = SoundManager.class.getResourceAsStream(soundPath);
                if (is == null) {
                    System.err.println("Sound file not found: " + soundPath);
                    return null;
                }

                // Use buffered stream for better performance
                BufferedInputStream bis = new BufferedInputStream(is);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bis);

                // Get clip and open it with the audio stream
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                // Cache the clip for future use
                clipCache.put(soundPath, clip);
            } catch (UnsupportedAudioFileException e) {
                System.err.println("Audio file format not supported: " + e.getMessage());
                return null;
            } catch (LineUnavailableException e) {
                System.err.println("Audio line unavailable: " + e.getMessage());
                return null;
            } catch (IOException e) {
                System.err.println("IO error loading sound: " + e.getMessage());
                return null;
            }
        }
        return clipCache.get(soundPath);
    }

    /**
     * Enables or disables all game sounds
     */
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
        if (!enabled) {
            // Stop all playing clips
            for (Clip clip : clipCache.values()) {
                if (clip.isRunning()) {
                    clip.stop();
                }
            }
        }
    }

    /**
     * Preloads a set of sounds for faster playback
     */
    public static void preloadSounds(String... soundPaths) {
        for (String path : soundPaths) {
            getClip(path);
        }
    }

    /**
     * Cleans up resources when the application is closing
     * Should be called before application exit
     */
    public static void cleanup() {
        for (Clip clip : clipCache.values()) {
            clip.close();
        }
        clipCache.clear();
    }
}