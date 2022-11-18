package org.ort_rehovot.bubble_shooter;

import lombok.Getter;
import lombok.Setter;
import org.ort_rehovot.bubble_shooter.ipc.NetworkClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GlobalState {
    private static GlobalState instance = null;

    public static GlobalState getInstance() {
        if (instance == null) {
            instance = new GlobalState();
        }
        return instance;
    }

    private boolean paused;
    private int score;
    @Setter
    @Getter
    private GamePanel gp;
    @Getter
    private boolean singlePlayer;

    //@Getter
    //private NetworkClient networkClient = null;

    private GlobalState() {
        paused = false;
        score = 0;
        singlePlayer = true;
        serverPort = -1;
    }

    @Getter
    private int serverPort;

    private int rivalX;
    private int rivalY;

    @Getter
    private InetSocketAddress rivalAddress;

    void initMultiPlayer(int port, String rivalIP, int rivalPort) throws SocketException, UnknownHostException {
        singlePlayer = false;
        serverPort = port;
        System.out.println("serverPort:"+serverPort);
        rivalX = -1;
        rivalY = -1;
        System.out.println("rivalIP:"+rivalIP);
        System.out.println("rivalPort:"+rivalPort);
        this.rivalAddress = new InetSocketAddress(rivalIP, rivalPort);
        //  networkClient = new NetworkClient(port);
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public synchronized void setPaused(boolean paused) {
        this.paused = paused;
    }

    public synchronized int getScore() {
        return score;
    }

    public synchronized void setScore(int score) {
        this.score = score;
    }

    public synchronized int getRivalX() {
        return rivalX;
    }

    public synchronized void setRivalX(int x) {
        this.rivalX = x;
    }

    public synchronized int getRivalY() {
        return rivalY;
    }

    public synchronized void setRivalY(int rivalY) {
        this.rivalY = rivalY;
    }

    public synchronized void updateScore(int score) {
        this.score += score;
    }
}
