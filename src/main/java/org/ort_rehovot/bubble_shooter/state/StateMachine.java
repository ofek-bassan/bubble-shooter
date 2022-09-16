package org.ort_rehovot.bubble_shooter.state;

import org.ort_rehovot.bubble_shooter.PlayingState;

import java.awt.*;

public class StateMachine {
    private AbstractState state;
    private static StateMachine sInstance = null;

    public static StateMachine getInstance() {
        if (sInstance == null) {
            sInstance = new StateMachine();
        }
        return sInstance;
    }

    private StateMachine() {
        state = new PlayingState();
    }

    public void repaint(Graphics g) {
        if (state != null) {
            state.repaint(g);
        }
    }

    public void handleMouseClick(int x, int y) {
        if (state != null) {
            state.handleMouseClick(x, y);
        }
    }

    public void handleEvent(Events ev) {
        AbstractState transition = state.transition(ev);
        if (transition != null) {
            state = transition;
        }
    }
}
