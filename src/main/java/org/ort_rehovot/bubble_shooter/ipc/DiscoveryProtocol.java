package org.ort_rehovot.bubble_shooter.ipc;

import java.net.InetAddress;
import java.util.*;

public class DiscoveryProtocol implements Protocol {

    int port1 = 0;
    int port2 = 0;

    InetAddress addr1;

    public DiscoveryProtocol() {
        addr1 = null;
    }

    @Override
    public List<Reply> handleCommand(InetAddress address, String data, LinkedList<Integer> ports) {
        String[] tokens = data.split(" ");
        if (tokens[0].equals("H")) {
            int p = Integer.parseInt(tokens[1]);
            if (port1 == 0) {
                port1 = ports.get(0);
                addr1 = address;
            } else {
                port2 = ports.get(1);
                Random random = new Random();
                long seed = random.nextLong();
                Reply r1 = new Reply();
                r1.setAddress(addr1);
                r1.setPort(port1);
                r1.setValue(CommandFormatter.ready(port2, seed, address.getHostAddress()));

                Reply r2 = new Reply();
                r2.setAddress(address);
                r2.setPort(port2);
                r2.setValue(CommandFormatter.ready(port1, seed, addr1.getHostAddress()));

                port1 = 0;
                return Arrays.asList(r1, r2);
            }
        }
        return new ArrayList<>();
    }
}
