package org.ort_rehovot.bubble_shooter.ipc;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class NetworkClient implements Closeable {
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    public NetworkClient(int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("192.168.1.26");
        this.port = port;
    }

    public NetworkClient(InetSocketAddress addr) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        this.address = addr.getAddress();
        this.port = addr.getPort();
    }

    public void send(String msg) throws IOException {
        byte[] buf = msg.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    public String receive() throws IOException {
        System.out.println("receiving the data....");
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        int len = 0;
        for (byte b : buf) {
            if (b == 0) {
                break;
            }
            len++;

        }
        return new String(buf, 0, len);
    }


    @Override
    public void close() {
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        try (NetworkClient client = new NetworkClient(4445)) {
            client.send("hello server");
            String echo = client.receive();
            System.out.println(echo);
            if (echo.equals("hello server")) {
                System.out.println("0-0000");
            }
            client.send("end");
        }
    }
}