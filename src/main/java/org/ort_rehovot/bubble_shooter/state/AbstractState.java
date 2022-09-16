package org.ort_rehovot.bubble_shooter.state;

public interface AbstractState {
    void say();

    AbstractState transition(char x);
}
