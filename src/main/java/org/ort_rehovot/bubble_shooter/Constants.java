package org.ort_rehovot.bubble_shooter;

import java.awt.*;
import java.io.IOException;

public class Constants {
    public static final int FIELD_SIZE_X = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int FIELD_SIZE_Y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    // game settings
    public static final int SPRITE_R = 50;
    public static final int PLAYER_X = FIELD_SIZE_X / 5+SPRITE_R;
    public static final int PLAYER_Y = FIELD_SIZE_Y - 2 * SPRITE_R;
    public static final int MAX_ROWS = 11;
    public static final int MAX_COLS = 28;
    public static final int MAX_BAD_THROWS = 1000;
    public static final int START_NUM_ROWS = 3;
    public static final int START_NUM_COLS = 14;
    public static int PLAYER_COLOR=1;
    public static int RIVAL_COLOR=1;
    public static int SECTOR=-2;
    public static int ROW=-2;
    public static int COLUMN=-2;

    public static final String IP = "192.168.1.70";    //  server ip
    public static final int DEFAULT_DISCOVERY_PORT = 4445; // discovery server port
    public static long SEED;    //game seed
    public static final  FrameController fc; // frame controller

    //border default settings
    public static final int BORDER_Y = -200;


    //setting that needs to be changed depends on the resolution
    public static final int BORDER_X = 894; // -----719 for laptop or school || 890 for 1080p || 742 for school-----
    public static final int BORDER_WIDTH_DRAW = 132;// -----95 for laptop or school || 130 for 1080p || 100 for school-----
    public static final int RIVAL_X = FIELD_SIZE_X / 2+SPRITE_R*10; // ------ 8 for laptop || 10 for 1080p-----
    public static final int BALL_WIDTH = 68; // -----54 for laptop 56 to school || 68 for 1080p-----
    public static final int RIVAL_BORDER_WIDTH = BORDER_X+BORDER_WIDTH_DRAW;
    static {
        try {
            fc = new FrameController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
