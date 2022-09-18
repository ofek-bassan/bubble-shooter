package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.state.Events;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    public GameFrame(Events ev) throws IOException {
        super();
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
