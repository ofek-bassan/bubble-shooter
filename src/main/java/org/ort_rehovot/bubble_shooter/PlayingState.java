package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.state.AbstractState;
import org.ort_rehovot.bubble_shooter.state.Events;

import java.awt.*;

public class PlayingState implements AbstractState {
    GameModel gameModel;
    GameController gameController;
    Arrow arrow;
    SoundSystem soundSystem;
    @Override
    public void repaint(Graphics g) {
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

    @Override
    public void handleMouseClick(int x, int y) {
        gameController.shoot(x, y);
    }

    @Override
    public AbstractState transition(Events ev) {
        switch (ev) {
            case GAME_OVER: return new GameOverState();
            case GAME_WIN: return new GameWinState();
            default:
                return null;
        }
    }
}
