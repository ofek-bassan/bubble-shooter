package org.ort_rehovot.bubble_shooter.ipc;

import org.ort_rehovot.bubble_shooter.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread {
    private final DatagramSocket socket;
    private boolean running;
    private final Protocol protocolHandler;
    private final LinkedList<Integer> ports;

    public Server(int port, Protocol protocolHandler) throws SocketException {
        socket = new DatagramSocket(port);
        ports = new LinkedList<>();
        this.protocolHandler = protocolHandler;
    }

    public void run() {
        running = true;
        System.out.println("Discovery server is up and running...");
        while (running) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();

                //packet = new DatagramPacket(buf, buf.length, address, port);
                int len = 0;
                for (byte b : buf) {
                    if (b == 0) {
                        break;
                    }
                    len++;

                }
                String received = new String(buf, 0, len);
                ports.add(packet.getPort());
                List<Protocol.Reply> toSend = protocolHandler.handleCommand(address, received,ports);
                System.out.println("port:"+packet.getPort());
                if (toSend == null) {
                    System.out.println("Got end");
                    running = false;
                } else {
                    if (!toSend.isEmpty()) {
                        reply(toSend.get(0).getAddress(),ports.get(0),toSend.get(0).getValue());
                        reply(toSend.get(1).getAddress(),ports.get(1),toSend.get(1).getValue());
                    }
                }
                //reply(address,packet.getPort(),received);

            } catch (Exception ignored) {
                System.out.println("Network error");
            }
        }
        socket.close();
    }

    private void reply(InetAddress address, int port, String data) {
        byte[] buf = data.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SocketException {
        int port = Constants.DEFAULT_DISCOVERY_PORT;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        new Server(port, new DiscoveryProtocol()).start();
    }
}
