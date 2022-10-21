package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.ao.ActiveObject;

import javax.sound.sampled.Clip;

public class SoundSystem extends ActiveObject {

    private static SoundSystem instance = null;

    public static SoundSystem getInstance() {
        if (instance == null) {
            instance = new SoundSystem();
        }
        return instance;
    }

    private final BackgroundMusic backgroundMusic;
    private final Thread bkMusicThread;

    private SoundSystem () {
        backgroundMusic = new BackgroundMusic();
        bkMusicThread = new Thread(backgroundMusic);
    }

    public void playBoom() {
        dispatch(() -> {
            try {
                Clip boom = ResourceLoader.getInstance().getBoom();
                boom.setFramePosition(0);
                boom.start();

            } catch (Exception exc) {
                exc.printStackTrace(System.out);
            }
        });
    }

    public void playBackgroundMusic() {
        bkMusicThread.start();
    }

    public void stopBackgroundMusic() {
        backgroundMusic.stop();
        try {
            bkMusicThread.join();
        } catch (InterruptedException ignored) {
        }
    }

    public void playExplosion() {
        dispatch(() -> {
            try {
                Clip boom = ResourceLoader.getInstance().getExplode();
                boom.setFramePosition(0);
                boom.start();

            } catch (Exception exc) {
                exc.printStackTrace(System.out);
            }
        });
    }
}
