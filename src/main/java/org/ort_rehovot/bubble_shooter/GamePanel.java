package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.ipc.Client;
import org.ort_rehovot.bubble_shooter.ipc.CommandFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends JPanel {
	GameModel gameModel;
	GameController gameController;
	Arrow arrow;
	private final JLabel pauseLabel;

	public void setPauseVisible(boolean v) {
		pauseLabel.setVisible(v);
	}

	public GamePanel() throws IOException {
		//hideMouseCursor();
		ResourceLoader.init();
		gameModel = new GameModel(this);
		gameController = new GameController(gameModel);
		arrow = new Arrow();
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
						gameController.shoot(x, y);
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
	}


	@Override
	public void paintComponent(Graphics g) {
		if (GlobalState.getInstance().isPaused()) {
			setPauseVisible(true);
		} else {
			setPauseVisible(false);
		}
		super.paintComponent(g);
		g.drawImage(ResourceLoader.getInstance().getBgImage(), 0, 0, getWidth(), getHeight(), null);
		gameModel.getPlayer().draw(g);
		Graphics2D g2d = (Graphics2D) g;
		arrow.paintComponent(g2d, getLocationOnScreen());



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
		try (Client client = new Client(port)) {
			System.out.println("Sending discovery of " + port);
			client.send(CommandFormatter.hello(port));
			String receive = client.receive();
			System.out.println(receive);
			Constants.SEED =Long.parseLong(receive.split(" ")[3]);
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
