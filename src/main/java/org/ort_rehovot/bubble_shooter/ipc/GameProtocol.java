package org.ort_rehovot.bubble_shooter.ipc;

import org.ort_rehovot.bubble_shooter.globals.Constants;
import org.ort_rehovot.bubble_shooter.globals.GlobalState;

import java.net.InetAddress;
import java.util.List;

public class GameProtocol implements Protocol {
    @Override
    public List<Reply> handleCommand(InetAddress address, String data) {

        String[] toks = data.split(" ");
        
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

        if (toks[0].equals("A")) {
            GlobalState.getInstance().setRemoteAnimationFinished(true);
        }

        if (toks[0].equals("SC")) {
            Constants.SECTOR = Integer.parseInt(toks[1]);
            Constants.ROW =Integer.parseInt(toks[2]);
            Constants.COLUMN =Integer.parseInt(toks[3]);
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
                GlobalState.getInstance().setRemoteAnimationFinished(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void sendSectorCord(int sector,int i, int j) {
        if (!GlobalState.getInstance().isSinglePlayer()) {
            String msg = String.format("SC %d %d %d", sector,i,j);
            try (NetworkClient client = new NetworkClient(GlobalState.getInstance().getRivalAddress())) {
                client.send(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void sendAnimationFinished() {
        if (!GlobalState.getInstance().isSinglePlayer()) {
            String msg = "A";
            try (NetworkClient client = new NetworkClient(GlobalState.getInstance().getRivalAddress())) {
                client.send(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
