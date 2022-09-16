package org.ort_rehovot.bubble_shooter.state;

import java.awt.*;

public interface AbstractState {
    void repaint(Graphics g);
    void handleMouseClick(int x, int y);

    AbstractState transition(Events ev);
}
