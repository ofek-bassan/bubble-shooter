package org.ort_rehovot.bubble_shooter;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ort_rehovot.bubble_shooter.ao.ActiveObject;
import org.ort_rehovot.bubble_shooter.ao.Command;

import java.io.IOException;
import java.util.List;

public class AnimationSystem extends Thread{
    @AllArgsConstructor
    private static class ExplodeCommand implements Command {
        private final GameModel gameModel;

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

    @Data
    @AllArgsConstructor
    private static class MovementState {
        int x;
        int y;
        double m;
        int w;
        int h;
        int dirx;
        int diry;
        int color;

        public void update() {
            if (x + Constants.BALL_WIDTH / 2 > w) {
                dirx *= -1;
            }

            if (x - Constants.BALL_WIDTH / 2 < 0) {
                dirx *= -1;
            }

            if (y + Constants.BALL_WIDTH / 2 > h) {
                diry *= -1;
            }

            if (y - Constants.BALL_WIDTH / 2 < 0) {
                diry *= -1;
            }

            if (m > 8 || m < -8) {
                y-=5;
            } else if (m < 3) {
                y = ((int) (m * ((x + 3) - x) + y)) * diry;
                x += (3 * dirx);
            }
        }

        public boolean checkThrow(GameModel gameModel) {
            return gameModel.checkCollision(x, y, 40, color);
        }
    }

    private enum State {
        IDLE,
        PLAYER_MOVING,
        RIVAL_MOVING,
        BOTH_MOVING,
        DONE
    }


    @Override
    public void run () {
        State myState = getInternalStateState();
        System.out.println("==================================================");
        gameModel.getView().repaint();
        while (myState!=State.DONE) {
            gameModel.getView().repaint();
            myState = getInternalStateState();
            System.out.println(myState);
            switch (myState) {
                case PLAYER_MOVING -> {
                    if (playerState.checkThrow(gameModel)) {
                        endPlayerShoot();
                        internalState = State.DONE;
                        endOrBoom();
                        gameModel.setNewPlayerOrRival(true);
                        break;
                    }
                    playerState.update();
                    gameModel.getPlayer().setX(playerState.x);
                    gameModel.getPlayer().setY(playerState.y);
                }
                case RIVAL_MOVING -> {
                    if (rivalState.checkThrow(gameModel)) {
                        endRivalShoot();
                        internalState = State.DONE;
                        gameModel.setNewPlayerOrRival(false);
                        break;
                    }
                    rivalState.update();
                    gameModel.getRivalPlayer().setX(rivalState.x);
                    gameModel.getRivalPlayer().setY(rivalState.y);
                }
                case BOTH_MOVING -> {
                    boolean player_collide = playerState.checkThrow(gameModel);
                    boolean rival_collide = rivalState.checkThrow(gameModel);
                    if (rival_collide && !player_collide) {
                        endRivalShoot();
                        internalState = State.PLAYER_MOVING;
                        gameModel.setNewPlayerOrRival(false);
                    } else if (!rival_collide && player_collide) {
                        endPlayerShoot();
                        internalState = State.RIVAL_MOVING;
                        gameModel.setNewPlayerOrRival(true);
                    } else if (rival_collide && player_collide) {
                        endRivalShoot();
                        endPlayerShoot();
                        internalState = State.DONE;
                        gameModel.setNewPlayerOrRival(true);
                        gameModel.setNewPlayerOrRival(false);
                    }
                    if(!player_collide)
                        playerState.update();
                    if(!rival_collide)
                        rivalState.update();
                }
            }
            try {
                Thread.sleep(3);
            } catch (InterruptedException ignored) {
                return;
            }
            refresh();
        }

    }

    private State getInternalStateState() {
        synchronized (this) {
            return internalState;
        }
    }

    private void endOrBoom()
    {
        boom();
        internalState = State.IDLE;
        if(gameModel.isGameOver())
        {
            try {
                Constants.fc.Lose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private MovementState playerState;
    private MovementState rivalState;
    private State internalState;

    private final GameModel gameModel;

    ActiveObject activeObject;

    public AnimationSystem(GameModel gameModel) {
        playerState = null;
        rivalState = null;
        this.gameModel = gameModel;
        activeObject = new ActiveObject();
        internalState = State.IDLE;
        gameModel.getView().repaint();
    }

    private void endPlayerShoot() {
        synchronized (this) {
            playerState = null;
        }
    }

    private void endRivalShoot() {
        synchronized (this) {
            rivalState = null;
        }
    }



    public void playerShoot(double m, int w, int h, int color) {
        synchronized (this) {
            if (playerState == null) {
                playerState = new MovementState(Constants.PLAYER1_X,
                        Constants.PLAYER_Y, m<0?m:m*-1, w, h, m>0?-1:1, 1, color);
                if(internalState == State.IDLE)
                    internalState = State.PLAYER_MOVING;
                else
                    internalState = State.BOTH_MOVING;
            }
        }
    }

    public void rivalShoot(double m, int w, int h, int color) {
        synchronized (this) {
            refresh();
            if (rivalState == null) {
                rivalState = new MovementState(Constants.PLAYER1_X,
                        Constants.PLAYER_Y, m<0?m:m*-1, w, h, m>0?-1:1, 1, color);
                if(internalState == State.IDLE)
                    internalState = State.RIVAL_MOVING;
                else
                    internalState = State.BOTH_MOVING;
            }
        }
    }

    public void boom(){
        activeObject.dispatch(new ExplodeCommand(gameModel));
    }

    private void refresh() {
        gameModel.getView().repaint();
    }
}
