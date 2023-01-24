package org.ort_rehovot.bubble_shooter.ipc;

import java.net.InetAddress;
import java.util.*;

public class DiscoveryProtocol implements Protocol {

    int port1 = 0;
    int port2 = 0;
    int w1, h1;
    int w2, h2;
    int color1,color2;

    InetAddress addr1;

    public DiscoveryProtocol() {
        addr1 = null;
    }

    @Override
    public List<Reply> handleCommand(InetAddress address, String data) {
        String[] tokens = data.split(" ");
        if (tokens[0].equals("H")) {
            int p = Integer.parseInt(tokens[1]);
            if (port1 == 0) {
                port1 = p;
                w1 = Integer.parseInt(tokens[2]);
                h1 = Integer.parseInt(tokens[3]);
                color1= Integer.parseInt(tokens[4]);
                addr1 = address;
            } else {
                port2 = Integer.parseInt(tokens[1]);
                w2 = Integer.parseInt(tokens[2]);
                h2 = Integer.parseInt(tokens[3]);
                color2= Integer.parseInt(tokens[4]);
                Random random = new Random();
                long seed = random.nextLong();
                Reply r1 = new Reply();
                r1.setAddress(addr1);
                r1.setPort(port1);
                r1.setColor(color1);

                r1.setValue(CommandFormatter.ready(port2, seed, address.getHostAddress(), w2, h2,color2));

                Reply r2 = new Reply();
                r2.setAddress(address);
                r2.setPort(port2);
                r2.setPort(color2);
                r2.setValue(CommandFormatter.ready(port1, seed, addr1.getHostAddress(), w1, h1,color1));

                port1 = 0;
                return Arrays.asList(r1, r2);
            }
        }
        return new ArrayList<>();
    }
}
