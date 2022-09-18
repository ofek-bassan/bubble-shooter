package org.ort_rehovot.bubble_shooter.ao;

import java.io.IOException;

public interface Command {
    void call() throws IOException;
}
