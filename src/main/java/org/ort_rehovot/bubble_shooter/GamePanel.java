package org.ort_rehovot.bubble_shooter;


import lombok.Getter;
import org.ort_rehovot.bubble_shooter.ipc.GameProtocol;
import org.ort_rehovot.bubble_shooter.ipc.NetworkClient;
import org.ort_rehovot.bubble_shooter.ipc.CommandFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;



public class GamePanel extends JPanel {
    GameModel gameModel;
    @Getter
    GameController gameController;
    Arrow arrowP1;
    Arrow arrowP2;
    @Getter
    private final JLabel pauseLabel;

    public void setPauseVisible(boolean v) {
        pauseLabel.setVisible(v);
    }

    public GamePanel() throws IOException {
        //hideMouseCursor();
        ResourceLoader.init();
        gameModel = new GameModel(this);
        gameController = new GameController(gameModel, GlobalState.getInstance().getServerPort());
        arrowP1 = new Arrow();
        arrowP2 = new Arrow(Constants.RIVAL_X);
        SoundSystem.getInstance().playBackgroundMusic();

        //  pause image
        pauseLabel = new JLabel(new ImageIcon(ResourceLoader.getInstance().getPauseImage()));
        pauseLabel.setBounds(0, 0, 500, 500);
        pauseLabel.setVisible(false);
        add(pauseLabel);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!GlobalState.getInstance().isPaused()) {
                    GameProtocol.sendMove(e.getX(), e.getY());
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!GlobalState.getInstance().isPaused()) {
                    if (e.getButton() == 1) {
                        int x = e.getX();
                        int y = e.getY();
                        gameController.shoot(x, y,true);
                        GameProtocol.sendShoot();
                    } else if (e.getButton() == 3) {
                        gameController.changeColor();
                    } else if (e.getButton() == 2) {
                        gameModel.printDebug();
                    }
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                gameModel.setWidth(e.getComponent().getWidth());
                gameModel.setHeight(e.getComponent().getWidth());
            }
        });
        GlobalState.getInstance().setGp(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        //setPauseVisible(GlobalState.getInstance().isPaused());
        super.paintComponent(g);
        g.drawImage(ResourceLoader.getInstance().getBgImage(), 0, 0, getWidth(), getHeight(), null);
        g.drawImage(ResourceLoader.getInstance().getBorderImage(), Constants.BORDER_X, Constants.BORDER_Y
                , getWidth(), getHeight() * 2, null);
        gameModel.getPlayer().draw(g);
        gameModel.getRivalPlayer().draw(g);
        Graphics2D g2d = (Graphics2D) g;
        arrowP1.paintComponent(g2d, getLocationOnScreen());
        //System.out.println("("+GlobalState.getInstance().getRivalX()+","+GlobalState.getInstance().getRivalY()+")");
        arrowP2.paintComponent(g2d, new Point(GlobalState.getInstance().getRivalX(),GlobalState.getInstance().getRivalY()));
        for (int i = 0; i < Constants.MAX_ROWS; i++) {

            for (int j = 0; j < Constants.MAX_COLS; j++) {
                gameModel.getGrid()[i][j].draw(g);
            }
        }
    }

    public void hideMouseCursor() {
        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JPanel.
        setCursor(blankCursor);
    }

    private static void waitForFriend(int port) throws IOException {
        int myServerPort = (int) (1000 + (65000 - 1000) * Math.random());

        try (NetworkClient client = new NetworkClient(port)) {
            System.out.println("Sending discovery of " + port);
            client.send(CommandFormatter.hello(myServerPort, Constants.FIELD_SIZE_X, Constants.FIELD_SIZE_Y));
            String receive = client.receive();
            System.out.println(receive);
            String[] toks = receive.split(" ");
            Constants.SEED = Long.parseLong(toks[3]);
            int rivalPort = Integer.parseInt(toks[1]);

            String rivalIp = toks[2];
            int w = Integer.parseInt(toks[4]);
            int h = Integer.parseInt(toks[5]);
            GlobalState.getInstance().initMultiPlayer(myServerPort,rivalIp,rivalPort, w, h);
        }
    }

    public static void main(String[] args) throws IOException {
		/*if (args.length == 1) {
			int port = Integer.parseInt(args[0]);
			waitForFriend(port);
		}

		 */

        Constants.fc.ShowGame();
    }



}
