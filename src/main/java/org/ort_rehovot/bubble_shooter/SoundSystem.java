package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.ao.ActiveObject;

import javax.sound.sampled.Clip;

public class SoundSystem extends ActiveObject {
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
        dispatch(() -> {
            try {
                Clip backgroundMusic = ResourceLoader.getInstance().getBackgroundMusic();
                backgroundMusic.setFramePosition(0);
                backgroundMusic.start();

            } catch (Exception exc) {
                exc.printStackTrace(System.out);
            }
        });
    }
    public void playExplosion() {

    }
}
