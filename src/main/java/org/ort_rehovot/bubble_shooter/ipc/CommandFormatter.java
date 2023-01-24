package org.ort_rehovot.bubble_shooter.ipc;

public class CommandFormatter {
    public static String hello(int port, int screenWidth, int screenHeight,int start_color) {
        return "H" + ' ' + port + ' ' + screenWidth + ' ' + screenHeight + ' ' + start_color;
    }

    public static String ready(int port, long seed, String ip, int screenWidth, int screenHeight,int start_color) {
        return "R" + ' ' + port + ' ' + ip + ' ' + seed + ' ' + screenWidth + ' ' + screenHeight + ' ' + start_color;
    }

}
