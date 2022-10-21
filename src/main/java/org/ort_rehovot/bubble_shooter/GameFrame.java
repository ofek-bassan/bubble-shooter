package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.state.Events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameFrame extends JFrame {

    public GameFrame(Events ev) throws IOException {
        super();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()== KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_Q)
                {
                    System.exit(0);
                }
            }
        });
        switch (ev)
        {
            case GAME_WIN:
            case START_GAME:
                GamePanel bbp = new GamePanel();
                add(bbp);
                break;
            case GAME_OVER:

                setBackground(Color.black);
                GameOverPanel gop = new GameOverPanel();

                add(gop);
                break;
        }
        /*
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.FIELD_SIZE_X, Constants.FIELD_SIZE_Y);
        setResizable(false);
        setVisible(true);

         */


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

}
