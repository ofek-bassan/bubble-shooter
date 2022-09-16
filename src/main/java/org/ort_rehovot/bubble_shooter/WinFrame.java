package org.ort_rehovot.bubble_shooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WinFrame extends JFrame implements ActionListener{
    //  image Logo
    private JLabel _img_menu;
    private JLabel _won_player;
    private JButton _exit_button;
    /**
     * Constructor
     */
    public WinFrame()
    {
        super();
        setLayout(null);

        //  winner label
        _won_player = new JLabel("You win!");
        _won_player.setFont(new Font("DialogInput", Font.PLAIN, 60));
        _won_player.setBounds(800, 0, 500, 100);
        _won_player.setForeground(Color.CYAN);
        add(_won_player);

        //  menu
        _img_menu = new JLabel(new ImageIcon(ResourceLoader.getInstance().getWinImage()));
        _img_menu.setBounds(0, 0, 500, 500);
        add(_img_menu);

        /*
        //  exit button
        _exit_button = new JButton(new ImageIcon("menuImg\\exit.png"));
        _exit_button.setBounds(890, 680, 120, 50);
        _exit_button.addActionListener(this);
        add(_exit_button);
         */
        setSize(Constants.FIELD_SIZE_X, Constants.FIELD_SIZE_Y);
        setVisible(true);
    }

    /**
     * move to another frame
     */
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        //MenuFrame mf = new MenuFrame();
    }
}