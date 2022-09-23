package org.ort_rehovot.bubble_shooter;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel{
    public GameOverPanel()
    {
        setLayout(null);
        setBackground(Color.black);
        //  winner label
        JLabel _won_player = new JLabel("YOU LOSE!!!!!");
        _won_player.setFont(new Font("DialogInput", Font.PLAIN, 60));
        _won_player.setBounds(Constants.FIELD_SIZE_Y/2, 0, 500, 100);
        _won_player.setForeground(Color.CYAN);
        add(_won_player);

        //  menu
        JLabel _img_menu = new JLabel(new ImageIcon(ResourceLoader.getInstance().getGameOverGif()));
        _img_menu.setBounds(Constants.FIELD_SIZE_Y/2 +100, 0, 500, 500);
        add(_img_menu);
    }
}
