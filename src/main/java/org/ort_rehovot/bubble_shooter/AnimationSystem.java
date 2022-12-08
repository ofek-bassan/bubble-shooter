package org.ort_rehovot.bubble_shooter;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ort_rehovot.bubble_shooter.ao.ActiveObject;
import org.ort_rehovot.bubble_shooter.ao.Command;

import java.io.IOException;
import java.util.List;

public class AnimationSystem extends Thread {
    @AllArgsConstructor
    private static class ExplodeCommand implements Command {
        private final GameModel gameModel;

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

        public void update() {
            System.out.println("("+x+","+y+")");
            if (x + Constants.BALL_WIDTH / 2 > borderEnd) {
                dirx *= -1;
            }

            if (x - Constants.BALL_WIDTH / 2 < borderStart) {
                dirx *= -1;
            }

            if (y + Constants.BALL_WIDTH / 2 > h) {
                diry *= -1;
            }

            if (m > 8 || m < -8) {
                y -= 5;
            } else if (m < 3) {
                y = ((int) (m * ((x + 3) - x) + y)) * diry;
                x += (3 * dirx);
            }
            System.out.println("("+x+","+y+")");
        }

        public boolean checkThrow(GameModel gameModel) {
            return gameModel.checkCollision(x, y, 40, color,isPlayer);
        }
    }

    private enum State {
        IDLE,
        PLAYER_MOVING,
        RIVAL_MOVING,
        BOTH_MOVING,
        DONE
    }


    private void doMove (MovementState state, boolean isPlayer) {
        if (state.checkThrow(gameModel)) {
            if (isPlayer)
                endPlayerShoot();
            else
                endRivalShoot();
            setInternalState(State.DONE);
            endOrBoom();
            gameModel.setNewPlayerOrRival(isPlayer);
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
        State myState = getInternalStateState();

        gameModel.getView().repaint();
        while (myState!=State.DONE) {
            myState = getInternalStateState();
//            if (myState != State.IDLE) {
//                System.out.println(myState);
//            }
            switch (myState) {
                case PLAYER_MOVING -> {
                    doMove(playerState, true);
                }

                case RIVAL_MOVING -> {
                    doMove(rivalState, false);
                }

                case BOTH_MOVING -> {
                    boolean player_collide = playerState.checkThrow(gameModel);
                    boolean rival_collide = rivalState.checkThrow(gameModel);
                    if (rival_collide && !player_collide) {
                        endRivalShoot();
                        setInternalState(State.PLAYER_MOVING);
                        gameModel.setNewPlayerOrRival(false);
                    } else if (!rival_collide && player_collide) {
                        endPlayerShoot();
                        setInternalState(State.RIVAL_MOVING);
                        gameModel.setNewPlayerOrRival(true);
                    } else if (rival_collide) {
                        endRivalShoot();
                        endPlayerShoot();
                        setInternalState(State.DONE);
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

    private void setInternalState(State s) {
        synchronized (this) {
            internalState = s;
        }
    }

    private void endOrBoom() {
        boom();
        setInternalState(State.IDLE);
        if (gameModel.isGameOver()) {
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


    public void playerShoot(double m, int BorderEND,int borderStart, int h, int color) {
        synchronized (this) {
            if (playerState == null) {
                playerState = new MovementState(Constants.PLAYER_X,
                        Constants.PLAYER_Y, m < 0 ? m : m * -1, BorderEND,borderStart, h, m > 0 ? -1 : 1, 1, color,true);
                if (internalState == State.IDLE)
                    internalState = State.PLAYER_MOVING;
                else
                    internalState = State.BOTH_MOVING;
            }
        }
    }

    public void rivalShoot(double m, int BorderEND,int borderStart, int h, int color) {
        synchronized (this) {
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

    public void boom() {
        activeObject.dispatch(new ExplodeCommand(gameModel));
    }

    private void refresh() {
        gameModel.getView().repaint();
    }
}
