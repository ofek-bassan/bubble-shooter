package org.ort_rehovot.bubble_shooter.threads;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ort_rehovot.bubble_shooter.globals.Constants;
import org.ort_rehovot.bubble_shooter.ao.ActiveObject;
import org.ort_rehovot.bubble_shooter.ao.Command;
import org.ort_rehovot.bubble_shooter.globals.GlobalState;
import org.ort_rehovot.bubble_shooter.ipc.GameProtocol;
import org.ort_rehovot.bubble_shooter.logic.Ball;
import org.ort_rehovot.bubble_shooter.logic.GameModel;

import java.io.IOException;
import java.util.List;

public class AnimationSystem extends Thread {
    @AllArgsConstructor
    private static class ExplodeCommand implements Command {
        private final GameModel gameModel;
        private final boolean isPlayer;

        @Override
        public void call() {
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
            if(!isPlayer)
                GameProtocol.sendAnimationFinished();
        }
    }

    @Data
    @AllArgsConstructor
    private static class MovementState {
        int x;
        int y;
        double m;
        int borderEnd;
        int borderStart;
        int h;
        int dirx;
        int diry;
        int color;
        boolean isPlayer;

            public synchronized void update() {
            if (x + Constants.BALL_WIDTH / 2 > borderEnd) {
                dirx *= -1;
            }

            if (x - Constants.BALL_WIDTH / 2 < borderStart) {
                dirx *= -1;
            }

            if (y + Constants.BALL_WIDTH / 2 > h) {
                diry *= -1;
            }

            if (m > 9 || m < -9) {
                y -= 5;
            } else if (m < 3) {
                y = ((int) (m * ((x + 3) - x) + y)) * diry;
                x += (3 * dirx);
            }
        }

        public synchronized boolean checkThrow(GameModel gameModel) {
            return gameModel.checkCollision(x, y, Constants.BALL_WIDTH, color,isPlayer);
        }
    }

    private enum State {
        IDLE,
        PLAYER_MOVING,
        RIVAL_MOVING,
        BOTH_MOVING,
        DONE
    }


    private synchronized void doMove (MovementState state, boolean isPlayer) {
        if (state.checkThrow(gameModel)) {
            if (isPlayer)
                endPlayerShoot();
            else
            {
                endRivalShoot();
            }
            if(getInternalStateState() == State.BOTH_MOVING)
            {
                if(isPlayer)
                    setInternalState(State.RIVAL_MOVING);
                else
                    setInternalState(State.PLAYER_MOVING);
            }
            else
                setInternalState(State.IDLE);
            gameModel.setNewPlayerOrRival(isPlayer);
            Boom(isPlayer);
            return;
        }
        state.update();
        if (isPlayer) {
            gameModel.getPlayer().setX(state.x);
            gameModel.getPlayer().setY(state.y);
        } else {
            gameModel.getRivalPlayer().setX(state.x);
            gameModel.getRivalPlayer().setY(state.y);
        }

    }

    @Override
    public void run () {
        State myState = checkWinGameOver();

        gameModel.getView().repaint();
        while (myState!=State.DONE) {
            if(!GlobalState.getInstance().isPaused()) {
                switch (myState) {
                    case PLAYER_MOVING -> doMove(playerState, true);

                    case RIVAL_MOVING -> doMove(rivalState, false);

                    case BOTH_MOVING -> {
                        doMove(rivalState, false);
                        doMove(playerState, true);

                    }
                }
                try {
                    Thread.sleep(3);
                } catch (InterruptedException ignored) {
                    return;
                }
                myState = checkWinGameOver();
            }
            refresh();
        }
    }

    private State getInternalStateState() {
        synchronized (this) {
            return internalState;
        }
    }

    private void setInternalState(State s) {
        synchronized (this) {
            internalState = s;
        }
    }

    private synchronized void Boom(boolean isPlayer) {
        boom(isPlayer);
    }

    private synchronized State checkWinGameOver() {
        boolean gamewinPlayer = gameModel.checkEnd(true), gamewinRival = gameModel.checkEnd(false);
        try {
            System.out.println("rival game over:" + GlobalState.getInstance().isRivalGameOver());
            if (gamewinPlayer || GlobalState.getInstance().isRivalGameOver()) // if player wins
            {
                Constants.fc.Win();
                return State.DONE;
            } else if (gamewinRival && !GlobalState.getInstance().isSinglePlayer() || GlobalState.getInstance().isPlayerGameOver()) //if rival wins or player loses
            {
                Constants.fc.Lose();
                return State.DONE;
            }
            return getInternalStateState();
        } catch (IOException e) {
            e.printStackTrace();
            return State.DONE;
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


    public void playerShoot(double m, int BorderEND,int borderStart, int h, int color) {
        synchronized (this) {
            //System.out.println(internalState);
            refresh();
            if (playerState == null) {
                playerState = new MovementState(Constants.PLAYER_X,
                        Constants.PLAYER_Y, m < 0 ? m : m * -1, BorderEND,borderStart, h, m > 0 ? -1 : 1, 1, color,true);
                if (internalState == State.IDLE)
                    internalState = State.PLAYER_MOVING;
                else
                    internalState = State.BOTH_MOVING;
            }
            else
            {
                System.out.println("error");
            }
        }
    }

    public void rivalShoot(double m, int BorderEND,int borderStart, int h, int color) {
        synchronized (this) {
            //System.out.println("state:"+internalState);
            refresh();
            if (rivalState == null) {
                rivalState = new MovementState(Constants.RIVAL_X,
                        Constants.PLAYER_Y, m < 0 ? m : -m, BorderEND,borderStart, h, m > 0 ? -1 : 1, 1, color,false);
                if (internalState == State.IDLE)
                    internalState = State.RIVAL_MOVING;
                else
                    internalState = State.BOTH_MOVING;
            }
        }
    }

    public void boom(boolean isPlayer) {
        activeObject.dispatch(new ExplodeCommand(gameModel,isPlayer));
    }

    private void refresh() {
        gameModel.getView().repaint();
    }
}
