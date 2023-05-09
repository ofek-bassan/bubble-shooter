package org.ort_rehovot.bubble_shooter;
import org.ort_rehovot.bubble_shooter.globals.Constants;
import org.ort_rehovot.bubble_shooter.resourceLoad.ResourceLoader;

import java.io.IOException;


public class App {

    public static void main(String[] args) throws IOException {
        Constants.SERVER_PORT = Integer.parseInt(args[0]);
        ResourceLoader.init();
        Constants.fc.ShowMenu();
    }
}
