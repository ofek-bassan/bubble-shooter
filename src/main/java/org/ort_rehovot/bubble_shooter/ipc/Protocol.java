package org.ort_rehovot.bubble_shooter.ipc;

import lombok.Data;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

public interface Protocol {

    @Data
    class Reply {
        private String value;
        private InetAddress address;
        private int port;
        private int w;
        private int h;
        private int color;
    }

    List<Reply> handleCommand(InetAddress address, String data);
}
