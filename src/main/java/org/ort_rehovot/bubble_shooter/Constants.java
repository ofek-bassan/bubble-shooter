package org.ort_rehovot.bubble_shooter;

import java.awt.*;
import java.io.IOException;

public class Constants {
    public static final int FIELD_SIZE_X = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int FIELD_SIZE_Y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public static final int SPRITE_R = 50;
    public static final int PLAYER_X = FIELD_SIZE_X / 2;
    public static final int PLAYER_Y = FIELD_SIZE_Y - 2 * SPRITE_R;
    public static final int BALL_WIDTH = 67;
    public static final int MAX_ROWS = 11;
    public static final int MAX_COLS = 28;
    public static final int MAX_BAD_THROWS = 1000;
    public static final  FrameController fc;

    static {
        try {
            fc = new FrameController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
