package org.ort_rehovot.bubble_shooter;


import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuPanel extends JPanel implements ActionListener {

    //  buttons
    private JButton exit;
    private JButton online;
    private JButton offline;

    public MenuPanel() throws IOException {
        super();
        ResourceLoader.init();
        setLayout(null);

        JButton  exit = new JButton (new ImageIcon(ResourceLoader.getInstance().getExit()));
        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        exit.setBounds(890, 500, 200, 50);
        exit.addActionListener(this);

        JButton  online = new JButton (new ImageIcon(ResourceLoader.getInstance().getOnline()));
        online.setBorderPainted(false);
        online.setContentAreaFilled(false);
        online.setFocusPainted(false);
        online.setBounds(890, 400, 200, 50);
        online.addActionListener(this);

        JButton  offline = new JButton (new ImageIcon(ResourceLoader.getInstance().getOffline()));
        offline.setBorderPainted(false);
        offline.setContentAreaFilled(false);
        offline.setFocusPainted(false);
        offline.setBounds(890, 300, 200, 50);
        offline.addActionListener(this);

        add(exit);
        add(online);
        add(offline);

    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        //  exit
        if(e.getSource() == exit)
        {
            System.exit(0);
            return;
        }

        //  online
        if(e.getSource() == online)
        {
            GlobalState.getInstance().setSinglePlayer(false);
            Constants.fc.ShowGame();
        }

        //  offline
        if(e.getSource() == offline)
        {
            GlobalState.getInstance().setSinglePlayer(true);
            Constants.fc.ShowGame();
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        //setPauseVisible(GlobalState.getInstance().isPaused());
        super.paintComponent(g);
        g.drawImage(ResourceLoader.getInstance().getBgImage(), 0, 0, getWidth(), getHeight(), null);
        Image image = ResourceLoader.getInstance().getLogo();
        g.drawImage(image, Constants.BORDER_X-350, 0
                , 800, 200, null);
    }
}
