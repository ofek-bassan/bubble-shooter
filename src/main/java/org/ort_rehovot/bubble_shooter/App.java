package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.ipc.CommandFormatter;
import org.ort_rehovot.bubble_shooter.ipc.NetworkClient;

import java.io.IOException;
import java.util.Random;

public class App {

    private static void waitForFriend(int port) throws IOException {
        int myServerPort = (int) (1000 + (65000 - 1000) * Math.random());

        try (NetworkClient client = new NetworkClient(port)) {
            Random rnd = new Random();
            int playerColor = rnd.nextInt(6) + 1;
            client.send(CommandFormatter.hello(myServerPort, Constants.FIELD_SIZE_X, Constants.FIELD_SIZE_Y,playerColor));
            String receive = client.receive();
            //System.out.println(receive);
            String[] toks = receive.split(" ");
            int rivalPort = Integer.parseInt(toks[1]);
            String rivalIp = toks[2];
            Constants.SEED = Long.parseLong(toks[3]);
            int w = Integer.parseInt(toks[4]);
            int h = Integer.parseInt(toks[5]);
            int rivalColor = Integer.parseInt(toks[6]);
            Constants.PLAYER_COLOR = playerColor;
            Constants.RIVAL_COLOR = rivalColor;
            GlobalState.getInstance().initMultiPlayer(myServerPort,rivalIp,rivalPort, w, h);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            int port = Integer.parseInt(args[0]);
            waitForFriend(port);
        }
        Constants.fc.ShowMenu();
    }
}
