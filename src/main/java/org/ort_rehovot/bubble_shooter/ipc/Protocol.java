package org.ort_rehovot.bubble_shooter.ipc;

import lombok.Data;

import java.net.InetAddress;
import java.util.List;

public interface Protocol {

    @Data
    class Reply {
        private String value;
        private InetAddress address;
        private int port;
    }

    List<Reply> handleCommand(InetAddress address, String data);
}
