package org.ort_rehovot.bubble_shooter;

import lombok.AllArgsConstructor;
import lombok.Data;

public abstract class AnimationSystem {

    @Data
    @AllArgsConstructor
    private class MovementState {
        int x;
        int y;
        double m;
        int w;
        int h;

        public void update() {

        }
    }

    private enum State {
        IDLE,
        PLAYER_MOVING,
        RIVAL_MOVING,
        BOTH_MOVING,
        DONE
    }



    void run () {
        while (true) {
            State myState = getState();
            if (myState == State.DONE) {
                return;
            }
            if (myState == State.PLAYER_MOVING) {
                playerState.update();
            }
            if (myState == State.RIVAL_MOVING) {
                rivalState.update();
            }
            if (myState == State.BOTH_MOVING) {
                playerState.update();
                rivalState.update();
            }
        }
    }

    private State getState() {
        synchronized (this) {
            return internalState;
        }
    }


    private MovementState playerState;
    private MovementState rivalState;
    private State internalState;

    public AnimationSystem() {
        playerState = null;
        rivalState = null;
        internalState = State.IDLE;
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



    public void playerShoot(double m, int w, int h) {
        synchronized (this) {
            if (playerState == null) {
                playerState = new MovementState(Constants.PLAYER1_X,
                        Constants.PLAYER_Y, m, w, h);
            }
        }
    }

    public void rivalShoot(double m, int w, int h) {
        synchronized (this) {
            if (rivalState == null) {
                rivalState = new MovementState(Constants.PLAYER1_X,
                        Constants.PLAYER_Y, m, w, h);
            }
        }
    }
}
