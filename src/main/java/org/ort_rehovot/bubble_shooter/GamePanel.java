package org.ort_rehovot.bubble_shooter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class GamePanel extends JPanel implements GameView{
	GameModel gameModel;
	GameController gameController;
	Arrow arrow;
	SoundSystem soundSystem;
	public GamePanel() throws IOException {
		ResourceLoader.init();
		gameModel = new GameModel(this);
		gameController = new GameController(gameModel);
		arrow = new Arrow();
		soundSystem = new SoundSystem();
		playBackgroundMusic();
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
		super.paintComponent(g);

		g.drawImage(ResourceLoader.getInstance().getBgImage(), 0, 0, getWidth(), getHeight(), null);
		gameModel.getPlayer().draw(g);
		Graphics2D g2d = (Graphics2D) g;
		arrow.paintComponent(g2d, getLocationOnScreen());



		for (int i = 0; i < gameModel.getRows(); i++) {
			for (int j = 0; j < gameModel.getCols(); j++) {
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

	public static void main(String[] args) throws IOException {
		Constants.fc.ShowGame();
		//GameFrame gf = new GameFrame();
		//WinFrame wf = new WinFrame();
		//GameOverPanel gop = new GameOverPanel();
		// bbp.hideMouseCursor();

	}

	@Override
	public void refresh() {
		repaint();
	}

	@Override
	public void playBoom() {
		soundSystem.playBoom();
	}

	public void playBackgroundMusic() {
		soundSystem.playBackgroundMusic();
	}
	@Override
	public void playExplosion() {
		soundSystem.playExplosion();
	}
}