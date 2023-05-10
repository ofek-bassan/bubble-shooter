package org.ort_rehovot.bubble_shooter.panels;


import lombok.Getter;
import lombok.SneakyThrows;
import org.ort_rehovot.bubble_shooter.globals.Constants;
import org.ort_rehovot.bubble_shooter.globals.GlobalState;
import org.ort_rehovot.bubble_shooter.ipc.GameProtocol;
import org.ort_rehovot.bubble_shooter.logic.Arrow;
import org.ort_rehovot.bubble_shooter.logic.GameModel;
import org.ort_rehovot.bubble_shooter.resourceLoad.ResourceLoader;
import org.ort_rehovot.bubble_shooter.threads.GameController;
import org.ort_rehovot.bubble_shooter.threads.SoundSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


    public class GamePanel extends JPanel implements ActionListener{
    @Getter
    GameModel gameModel;

    private final JButton exit;
    @Getter
    GameController gameController;
    Arrow arrowP1;
    Arrow arrowP2;
    @Getter
    private final JLabel pauseLabel;

    public GamePanel() throws IOException  {
        // hideMouseCursor();
        // ResourceLoader.init();
        setLayout(null);

        gameModel = new GameModel(this);
        gameController = new GameController(gameModel, GlobalState.getInstance().getServerPort());
        arrowP1 = new Arrow();
        if(!GlobalState.getInstance().isSinglePlayer())
            arrowP2 = new Arrow(Constants.RIVAL_X);
        SoundSystem.getInstance().playBackgroundMusic();

        exit = new JButton (new ImageIcon(ResourceLoader.getInstance().getExit()));
        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        exit.setBounds(0, Constants.FIELD_SIZE_Y-Constants.SPRITE_R, 200, 50);
        exit.addActionListener(this);
        add(exit);

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
                if (!GlobalState.getInstance().isPaused() && GlobalState.getInstance().isRemoteAnimationFinished()) {
                    if (e.getButton() == 1) {
                        int x = e.getX();
                        int y = e.getY();
                        double m = ((double) (y) - gameModel.getPlayer().getY()) / (x - gameModel.getPlayer().getX());
                        GameProtocol.sendShoot(m);
                        gameController.shoot(m,true);
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

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        Constants.fc.ShowMenu();
    }

    @Override
    public void paintComponent(Graphics g) {
        //setPauseVisible(GlobalState.getInstance().isPaused());
        super.paintComponent(g);
        g.drawImage(ResourceLoader.getInstance().getBgImage(), 0, 0, getWidth(), getHeight(), null);
        gameModel.getPlayer().draw(g);
        Graphics2D g2d = (Graphics2D) g;
        arrowP1.paintComponent(g2d, getLocationOnScreen());
        for (int i = 0; i < Constants.MAX_ROWS; i++) {
            for (int j = 0; j < Constants.MAX_COLS; j++) {
                gameModel.getGrid()[i][j].draw(g);
            }
        }
        if(!GlobalState.getInstance().isSinglePlayer())
        {
            arrowP2.paintComponent(g2d, new Point(GlobalState.getInstance().getRivalX(),GlobalState.getInstance().getRivalY()));
            Image image = ResourceLoader.getInstance().getBorderImage();
            g.drawImage(image, Constants.BORDER_X, Constants.BORDER_Y
                    , Constants.BORDER_WIDTH_DRAW, getHeight() * 2, null);
            gameModel.getRivalPlayer().draw(g);
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

}
