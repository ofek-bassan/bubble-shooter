package org.ort_rehovot.bubble_shooter.ipc;

import org.ort_rehovot.bubble_shooter.Constants;
import org.ort_rehovot.bubble_shooter.GlobalState;

import java.net.InetAddress;
import java.util.List;

public class GameProtocol implements Protocol {
    @Override
    public List<Reply> handleCommand(InetAddress address, String data) {
        String[] toks = data.split(" ");

        if (toks[0].equals("M")) {
            int x = Integer.parseInt(toks[1]);
            int y = Integer.parseInt(toks[2]);

            int xscaled = (int)(x * (float)Constants.FIELD_SIZE_X / (float)GlobalState.getInstance().getRivalW());
            int yscaled = (int)(y * (float)Constants.FIELD_SIZE_Y / (float)GlobalState.getInstance().getRivalH());



            GlobalState.getInstance().setRivalX(xscaled);

            System.out.printf("(%d, %d) -> (%d, %d) : %d %n", x, y, xscaled, yscaled, GlobalState.getInstance().getRivalX());

            GlobalState.getInstance().setRivalY(yscaled);
            GlobalState.getInstance().getGp().repaint();

        }

        if (toks[0].equals("S")) {
            int rivalX = GlobalState.getInstance().getRivalX();
            int rivalY = GlobalState.getInstance().getRivalY();
            GlobalState.getInstance().getGp().getGameController().shoot(rivalX,rivalY,false);

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

    public static void sendShoot() {
        if (!GlobalState.getInstance().isSinglePlayer()) {
            String msg = "S";
            try (NetworkClient client = new NetworkClient(GlobalState.getInstance().getRivalAddress())) {
                client.send(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
