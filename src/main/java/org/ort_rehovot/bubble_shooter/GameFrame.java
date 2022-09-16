package org.ort_rehovot.bubble_shooter;

import javax.swing.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    public GameFrame() throws IOException {
        super();
        GamePanel bbp = new GamePanel();
        add(bbp);
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
