package org.ort_rehovot.bubble_shooter.ipc;

public class CommandFormatter {
    public static String hello(int port) {
        return "H" + ' ' + port;
    }

    public static String ready(int port, int seed, String ip) {
        return "R" + ' ' + port + ' ' + ip + ' ' + seed;
    }

}
