package org.ort_rehovot.bubble_shooter;

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

    private GlobalState() {
        paused = false;
        score = 0;
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

    public synchronized void updateScore(int score) {
        this.score += score;
    }
}
