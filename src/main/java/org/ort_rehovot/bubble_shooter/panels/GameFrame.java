package org.ort_rehovot.bubble_shooter.panels;

import org.ort_rehovot.bubble_shooter.globals.GlobalState;
import org.ort_rehovot.bubble_shooter.threads.Events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameFrame extends JFrame {

    public GameFrame(Events ev) throws IOException {
        super();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()== KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_Q)
                {
                    System.exit(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE && GlobalState.getInstance().isSinglePlayer()) {
                    GlobalState.getInstance().setPaused(!GlobalState.getInstance().isPaused());
                    System.out.println("Paused");
                    repaint();
                }
            }
        });
        switch (ev) {
            case ShowMenu -> {
                MenuPanel bbp = new MenuPanel();
                add(bbp);
            }
            case START_GAME -> {
                GamePanel bbp = new GamePanel();
                add(bbp);
                GlobalState.getInstance().getGp().getGameModel().setNewPlayerOrRival(true);
                if(!GlobalState.getInstance().isSinglePlayer())
                    GlobalState.getInstance().getGp().getGameModel().setNewPlayerOrRival(false);
            }
            case GAME_OVER -> {
                setBackground(Color.black);
                GameOverPanel gop = new GameOverPanel("YOU LOSE!!!!!");
                add(gop);
            }
            case GAME_WIN -> {
                setBackground(Color.black);
                GameOverPanel gop = new GameOverPanel("YOU WIN!!!!!");
                add(gop);
            }
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

}
