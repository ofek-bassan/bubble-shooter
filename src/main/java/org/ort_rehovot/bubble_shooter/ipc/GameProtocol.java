package org.ort_rehovot.bubble_shooter.ipc;

import org.ort_rehovot.bubble_shooter.Constants;
import org.ort_rehovot.bubble_shooter.GlobalState;

import java.net.InetAddress;
import java.util.List;

public class GameProtocol implements Protocol {
    @Override
    public List<Reply> handleCommand(InetAddress address, String data) {

        String[] toks = data.split(" ");

        if (toks[0].equals("NC")) {
            int player_color = Integer.parseInt(toks[1]);
            int rival_color = Integer.parseInt(toks[2]);
            GlobalState.getInstance().getGp().getGameController().getGameModel().setRivalAndPlayerColor(player_color,rival_color);
        }
        
        if (toks[0].equals("M")) {
            int x = Integer.parseInt(toks[1]);
            int y = Integer.parseInt(toks[2]);

            int xscaled = (int) (x * (float) Constants.FIELD_SIZE_X / (float) GlobalState.getInstance().getRivalW());
            int yscaled = (int) (y * (float) Constants.FIELD_SIZE_Y / (float) GlobalState.getInstance().getRivalH());


            GlobalState.getInstance().setRivalX(xscaled);

            GlobalState.getInstance().setRivalY(yscaled);
            GlobalState.getInstance().getGp().repaint();

        }

        if (toks[0].equals("S")) {
            double m = Double.parseDouble(toks[1]);
            GlobalState.getInstance().getGp().getGameController().shoot(m, false);

        }

        if (toks[0].equals("RD")) {
            int newRow = Integer.parseInt(toks[1]);
            GlobalState.getInstance().getGp().getGameController().getGameModel().addRow(newRow, false);
        }



        return List.of();
    }


    public static void sendMove(int x, int y) {
        if (!GlobalState.getInstance().isSinglePlayer()) {
            String msg = String.format("M %d %d", x, y);
            try (NetworkClient client = new NetworkClient(GlobalState.getInstance().getRivalAddress())) {
                client.send(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void sendShoot(double m) {
        if (!GlobalState.getInstance().isSinglePlayer()) {
            String msg = String.format("S %f", m);
            try (NetworkClient client = new NetworkClient(GlobalState.getInstance().getRivalAddress())) {
                client.send(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void sendRowDown(int newRow) {
        if (!GlobalState.getInstance().isSinglePlayer()) {
            String msg = String.format("RD %d", newRow);
            try (NetworkClient client = new NetworkClient(GlobalState.getInstance().getRivalAddress())) {
                client.send(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void sendInitColor(int player_color,int rival_color) {
        if (!GlobalState.getInstance().isSinglePlayer()) {
            String msg = String.format("NC %d %d", player_color,rival_color);
            try (NetworkClient client = new NetworkClient(GlobalState.getInstance().getRivalAddress())) {
                client.send(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
