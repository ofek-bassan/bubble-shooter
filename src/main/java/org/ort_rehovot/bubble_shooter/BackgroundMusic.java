package org.ort_rehovot.bubble_shooter;

import javax.sound.sampled.Clip;

public class BackgroundMusic implements Runnable {

    private final Clip music;

    public BackgroundMusic () {
        music = ResourceLoader.getInstance().getBackgroundMusic();
        music.setFramePosition(0);

    }

    @Override
    public void run() {
        music.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        music.close();
    }
}
