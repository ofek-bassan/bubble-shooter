package org.ort_rehovot.bubble_shooter.ao;

import java.io.IOException;

public class ActiveObject extends Thread{
    private BoundedBuffer<Command> queue;

    public ActiveObject() {
           queue = new BoundedBuffer<>();
           start();
    }

    public void dispatch(Command cmd) {
        try {
            queue.put(cmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exit () throws InterruptedException {
        queue.put(null);
        this.join();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Command cmd = queue.take();
                if (cmd == null) {
                    return;
                }
                cmd.call();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
