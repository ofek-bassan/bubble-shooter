package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.ipc.NetworkClient;
import org.ort_rehovot.bubble_shooter.ipc.Protocol;

import java.net.InetAddress;
import java.util.List;

public class GameProtocol implements Protocol {
    @Override
    public List<Reply> handleCommand(InetAddress address, String data) {
        String[] toks = data.split(" ");

        if (toks[0].equals("M")) {
            int x = Integer.parseInt(toks[1]);
            int y = Integer.parseInt(toks[2]);
            System.out.println(x + ":" + y);
        }

        return List.of();
    }


    public static void sendMove(int x , int y) {
        if (!GlobalState.getInstance().isSinglePlayer()) {
            String msg = String.format("M %d %d", x, y);
            try (NetworkClient client = new NetworkClient(GlobalState.getInstance().getRivalAddress())) {
                client.send(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
