package org.ort_rehovot.bubble_shooter.panels;


import lombok.SneakyThrows;
import org.ort_rehovot.bubble_shooter.globals.Constants;
import org.ort_rehovot.bubble_shooter.globals.GlobalState;
import org.ort_rehovot.bubble_shooter.resourceLoad.ResourceLoader;
import org.ort_rehovot.bubble_shooter.ipc.CommandFormatter;
import org.ort_rehovot.bubble_shooter.ipc.NetworkClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

public class MenuPanel extends JPanel implements ActionListener {

    //  buttons
    private final JButton exit;
    private final JButton online;
    private final JButton offline;

    public MenuPanel(){
        super();
        setLayout(null);

        exit = new JButton (new ImageIcon(ResourceLoader.getInstance().getExit()));
        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        exit.setBounds(890, 500, 200, 50);
        exit.addActionListener(this);

        online = new JButton (new ImageIcon(ResourceLoader.getInstance().getOnline()));
        online.setBorderPainted(false);
        online.setContentAreaFilled(false);
        online.setFocusPainted(false);
        online.setBounds(890, 400, 200, 50);
        online.addActionListener(this);

        offline = new JButton (new ImageIcon(ResourceLoader.getInstance().getOffline()));
        offline.setBorderPainted(false);
        offline.setContentAreaFilled(false);
        offline.setFocusPainted(false);
        offline.setBounds(890, 300, 200, 50);
        offline.addActionListener(this);

        add(exit);
        add(online);
        add(offline);

    }


    private static void waitForFriend() throws IOException {
        int myServerPort = (int) (1000 + (65000 - 1000) * Math.random());
        //Constants.PLAYER_BALL_LIST = makeBallList();
        try (NetworkClient client = new NetworkClient(Constants.SERVER_PORT)) {
            Random rnd = new Random();
            int playerColor = rnd.nextInt(6) + 1;
            client.send(CommandFormatter.hello(myServerPort, Constants.FIELD_SIZE_X, Constants.FIELD_SIZE_Y,playerColor));
            String receive = client.receive();
            //System.out.println(receive);
            String[] toks = receive.split(" ");
            int rivalPort = Integer.parseInt(toks[1]);
            String rivalIp = toks[2];
            Constants.SEED = Long.parseLong(toks[3]);
            int w = Integer.parseInt(toks[4]);
            int h = Integer.parseInt(toks[5]);
            int rivalColor = Integer.parseInt(toks[6]);
            Constants.PLAYER_COLOR = playerColor;
            Constants.RIVAL_COLOR = rivalColor;
            //Constants.RIVAL_BALL_LIST = makeBallList();
            GlobalState.getInstance().initMultiPlayer(myServerPort,rivalIp,rivalPort, w, h);
            System.out.println("good");
        }
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
            waitForFriend();
            Constants.fc.ShowGame();
        }

        //  offline
        if(e.getSource() == offline)
        {
            GlobalState.getInstance().setSinglePlayer(true);
            Constants.PLAYER_X = Constants.FIELD_SIZE_X/2;
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
