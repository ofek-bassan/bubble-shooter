package org.ort_rehovot.bubble_shooter.panels;

import org.ort_rehovot.bubble_shooter.panels.GameFrame;
import org.ort_rehovot.bubble_shooter.threads.Events;
import org.ort_rehovot.bubble_shooter.threads.SoundSystem;

import java.io.IOException;

    public class FrameController {
    private GameFrame gf;
    public FrameController() throws IOException {
        //gf = new GameFrame(Events.START_GAME);
    }

    public void ShowMenu() throws IOException {
        gf = new GameFrame(Events.ShowMenu);
    }

    public void ShowGame() throws IOException {
        gf.dispose();
        gf = new GameFrame(Events.START_GAME);
    }

    public void Lose() throws IOException {
        SoundSystem.getInstance().stopBackgroundMusic();
        gf.dispose();
        gf = new GameFrame(Events.GAME_OVER);
    }

    public void WIN() throws IOException {
        SoundSystem.getInstance().stopBackgroundMusic();
        gf.dispose();
        gf = new GameFrame(Events.GAME_WIN);
    }

    public void Win() throws IOException {
        gf.dispose();
        gf = new GameFrame(Events.GAME_WIN);
    }
}
