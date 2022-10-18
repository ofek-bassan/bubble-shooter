package org.ort_rehovot.bubble_shooter;

import lombok.AllArgsConstructor;
import lombok.val;
import org.ort_rehovot.bubble_shooter.ao.ActiveObject;
import org.ort_rehovot.bubble_shooter.ao.Command;

import java.io.IOException;
import java.util.List;


public class GameController {
    private final GameModel gameModel;
    private boolean inAnimation1 = false;
    private final ActiveObject activeObject = new ActiveObject();

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public synchronized boolean isInAnimation() {
        return inAnimation1;
    }

    public synchronized void setInAnimation(boolean v) {
        inAnimation1 = v;
    }

    @AllArgsConstructor
    private static class ExplodeCommand implements Command {
        private final GameModel gameModel;
        private final GameController owner;

        @Override
        public void call() throws IOException {
            List<Ball> ballsToExplode = gameModel.hasBallsToExplode();
            if (!ballsToExplode.isEmpty()) {
                do {
                    for (Ball b : ballsToExplode) {
                        b.advanceExplosionAnimation();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gameModel.getView().repaint();
                    ballsToExplode = gameModel.hasBallsToExplode();
                } while (!ballsToExplode.isEmpty());
            }
        }
    }


    private static class PlayerMoved implements Command {
        private final GameModel gameModel;
        private final GameController owner;
        private int dirx = 1;
        private int diry = 1;

        /***
         * constructor
         * @param gameModel panel
         * @param gameController controller
         */
        public PlayerMoved(GameModel gameModel, GameController gameController) {
            this.gameModel = gameModel;
            this.owner = gameController;
        }

        /***
         * moves the ball until it collided with another ball
         */



        @Override
        public void call() throws IOException {
            owner.setInAnimation(true);
            val h = gameModel.getHeight();
            val w = gameModel.getWidth();

            double m = gameModel.getPlayer().getSlope();
            while (true) {
                int x = gameModel.getPlayer().getX();
                int y = gameModel.getPlayer().getY();
                if (checkThrow(gameModel)) {
                    break;
                }
                if (h > 0) // until panel gets size from frame 23-5-2021
                {
                    if (x + Constants.BALL_WIDTH / 2 > w) {
                        dirx = -1;
                    }

                    if (x - Constants.BALL_WIDTH / 2 < 0) {
                        dirx = -1;
                    }

                    if (y + Constants.BALL_WIDTH / 2 > h) {
                        diry = -1;
                    }

                    if (y - Constants.BALL_WIDTH / 2 < 0) {
                        diry = 1;
                    }

                    if (m > 8 || m < -8) {
                        gameModel.getPlayer().addY(-4);
                    } else if (m < 3) {
                        gameModel.getPlayer().setY(((int) (m * ((x + 3) - x) + y)) * diry);
                        if (!gameModel.getPlayer().isLeft()) {
                            gameModel.getPlayer().addX((3 * dirx));
                        } else {
                            gameModel.getPlayer().addX((-3 * dirx));
                        }
                    }
                }
                try {
                    Thread.sleep(3);
                } catch (InterruptedException ignored) {
                    return;
                }
                gameModel.getView().repaint();
            }
            owner.setInAnimation(false);
            if(gameModel.isGameOver())
            {
                try {
                    Constants.fc.Lose();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                gameModel.getView().repaint();
                owner.activeObject.dispatch(new ExplodeCommand(gameModel, owner));
                gameModel.setNewPlayer();
            }
        }
    }

    /***
     * shoots the ball if there is no any other ball thrown when the ball was thrown
     * @param x x position
     * @param y y position
     */
    public void shoot(int x, int y) {
        val player = gameModel.getPlayer();
        if (isInAnimation() || player == null) {
            return;
        }
        double m = ((double) (y) - player.getY()) / (x - player.getX());
        //send "pressed" signal to controller
        gameModel.getPlayer().setSlope(m);
        gameModel.getPlayer().setActivated(true);

        activeObject.dispatch(new PlayerMoved(gameModel, this));
        setInAnimation(false);
    }

    /***
     * checks if there was a collision
     * @param gameModel panel
     * @return true iff there was a collision
     */
    public static boolean checkThrow(GameModel gameModel) {
        return gameModel.checkCollision(
                gameModel.getPlayer().getX(),
                gameModel.getPlayer().getY()
                , 40,
                gameModel.getPlayer().getColor());
    }

    public void changeColor() {
        Ball player = gameModel.getPlayer();
        player.changeColor();
        gameModel.getView().repaint();
    }
}