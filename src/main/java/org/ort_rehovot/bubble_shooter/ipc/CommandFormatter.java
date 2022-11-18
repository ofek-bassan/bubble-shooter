package org.ort_rehovot.bubble_shooter.ipc;

public class CommandFormatter {
    public static String hello(int port, int screenWidth, int screenHeight) {
        return "H" + ' ' + port + ' ' + screenWidth + ' ' + screenHeight;
    }

    public static String ready(int port, long seed, String ip, int screenWidth, int screenHeight) {
        return "R" + ' ' + port + ' ' + ip + ' ' + seed + ' ' + screenWidth + ' ' + screenHeight;
    }

}
