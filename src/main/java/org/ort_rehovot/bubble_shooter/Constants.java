package org.ort_rehovot.bubble_shooter;

import java.awt.*;
import java.io.IOException;

public class Constants {
    public static final int FIELD_SIZE_X = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int FIELD_SIZE_Y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    //border
    public static final int BORDER_X = 890;
    public static final int BORDER_WIDTH_DRAW = 130;
    public static final int BORDER_Y = -200;
    public static final int BORDER_WIDTH = 920; // -----720 for laptop or school || 920 for 1080p-----
    public static final int RIVAL_BORDER_WIDTH = 970; // -----1120 for laptop or school || 920 for 1080p-----

    public static final int SPRITE_R = 50;
    //public static final int PLAYER_X = FIELD_SIZE_X / 2;
    //public static final int START_NUM_COLS = 28;
    public static final int PLAYER_X = FIELD_SIZE_X / 5+SPRITE_R;
    public static final int PLAYER_Y = FIELD_SIZE_Y - 2 * SPRITE_R;
    //public static final int RIVAL_X = FIELD_SIZE_X / 2+SPRITE_R*10; ---------------1080p
    public static final int RIVAL_X = FIELD_SIZE_X / 2+SPRITE_R*10;
    public static final int BALL_WIDTH = 68; // -----54 for laptop 56 to school || 68 for 1080p-----
    public static final int MAX_ROWS = 11;
    public static final int MAX_COLS = 28;
    public static final int MAX_BAD_THROWS = 3;
    public static final int START_NUM_ROWS = 3;
    public static final int START_NUM_COLS = 14;
    public static final String IP = "192.168.1.61";
    public static long SEED;
    public static final  FrameController fc;

    public static final int DEFAULT_DISCOVERY_PORT = 4445;

    static {
        try {
            fc = new FrameController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
