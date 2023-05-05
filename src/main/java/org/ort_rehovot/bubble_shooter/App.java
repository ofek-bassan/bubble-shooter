package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.ipc.CommandFormatter;
import org.ort_rehovot.bubble_shooter.ipc.NetworkClient;

import java.io.IOException;
import java.util.Random;

public class App {

    public static void main(String[] args) throws IOException {
        Constants.SERVER_PORT = Integer.parseInt(args[0]);
        ResourceLoader.init();
        Constants.fc.ShowMenu();
    }
}
