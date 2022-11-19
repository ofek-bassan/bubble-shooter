package org.ort_rehovot.bubble_shooter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;

/**
 * Realises the moving arrow at the bottom of the screen.
 * @author Barni
 *
 */
public class Arrow{

    /**
     * current location of the mouse pointer
     */
    private Point p;

    private static final int TIP_LENGTH = 20;
    private int player_x = Constants.PLAYER1_X;
    private int arrowX = Constants.PLAYER1_X - 50 / 2;
    private static final int arrowY = Constants.PLAYER_Y - 140 - Constants.SPRITE_R/2;
    private static final int LENGTH = 80;
    private boolean rival;

    /**
     * Constructor for the class arrow.
     */
    public Arrow(){
        rival = false;
        p = new Point(Constants.FIELD_SIZE_X/2,600);
    }
    /**
     * Constructor for the class arrow.
     */
    public Arrow(int x){
        rival = true;
        arrowX = x - 50 / 2;
        player_x = Constants.PLAYER2_X;
        p = new Point(Constants.FIELD_SIZE_X/2,600);
        //p = new Point(Constants.FIELD_SIZE_X/2,600);
    }

    /**
     * Paints the arrow on the bottom of the screen depending on
     * the location of the pointer.
     * @param g2d the graphics where the arrow should be drawn
     * @param base the location of the frame on the screen
     */
    public void paintComponent(Graphics2D g2d, Point base) {
        g2d.setColor(Color.red);
        int x, y;
        if (!rival) {
            Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
            x = mouseLoc.x - base.x;
            y = mouseLoc.y - base.y;

            if ((0 <= x) && (x < Constants.FIELD_SIZE_X) && (0 <= y) && (y < Constants.FIELD_SIZE_Y)) {
                p = mouseLoc;
            }
            x = p.x - base.x;
            y = p.y - base.y;
        } else {
            x = p.x + base.x;
            y = p.y + base.y;
        }
        double angle = Math.atan((double)(x-player_x)/(Constants.PLAYER_Y));
        System.out.println(angle);
        /*if(player_x!=Constants.PLAYER1_X)
        {
            angle+=225;
        }
         */
        g2d.rotate(angle,player_x,Constants.PLAYER_Y);
        g2d.drawImage(ResourceLoader.getInstance().getArrow(), arrowX, arrowY, 50, 140, null);
        g2d.rotate(-angle,player_x,Constants.PLAYER_Y);
    }

}
