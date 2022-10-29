package org.ort_rehovot.bubble_shooter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameOverFrame extends JFrame{
    /**
     * Constructor
     */
    public GameOverFrame()
    {
        super();
        setLayout(null);
        setBackground(Color.black);
        //  winner label
        JLabel _won_player = new JLabel("YOU LOSE:(((((!");
        _won_player.setFont(new Font("DialogInput", Font.PLAIN, 60));
        _won_player.setBounds(800, 0, 500, 100);
        _won_player.setForeground(Color.CYAN);
        add(_won_player);

        //  menu
        //  image Logo
        JLabel _img_menu = new JLabel(new ImageIcon(ResourceLoader.getInstance().getGameOverGif()));
        _img_menu.setBounds(0, 0, 500, 500);
        add(_img_menu);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

    /**
     * move to another frame
     */
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }
}
