package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.state.StateMachine;

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
					StateMachine.getInstance().handleMouseClick(x, y);
					//gameController.shoot(x, y);
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
		StateMachine.getInstance().repaint(g);
//
//


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

		GameFrame gf = new GameFrame();
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
