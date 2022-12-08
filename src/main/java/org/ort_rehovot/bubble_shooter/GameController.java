package org.ort_rehovot.bubble_shooter;

import org.ort_rehovot.bubble_shooter.ipc.GameProtocol;
import org.ort_rehovot.bubble_shooter.ipc.Server;

import java.net.SocketException;


public class GameController {
    private final GameModel gameModel;
    private final AnimationSystem animationSystem;
    private final Server server;

    public GameController(GameModel gameModel, int port) throws SocketException {
        this.gameModel = gameModel;
        animationSystem = new AnimationSystem(gameModel);
        animationSystem.start();
        if (port != -1) {
            System.out.println("Starting server on port " + port);
            server = new Server(port, new GameProtocol());
            server.start();
        } else {
            server = null;
        }
    }

    /***
     * shoots the ball if there is no any other ball thrown when the ball was thrown
     * @param x x position
     * @param y y position
     */
    public void shoot(int x, int y,boolean isPlayer) {
        if(isPlayer)
        {
            Ball player = gameModel.getPlayer();
            double m = ((double) (y) - player.getY()) / (x - player.getX());
            System.out.println("slope player:"+m);
            animationSystem.playerShoot(m,Constants.BORDER_WIDTH,0,gameModel.getHeight(),player.getColor());
        }
        else
        {
            Ball rivalPlayer = gameModel.getPlayer();
            int py = rivalPlayer.getY();
            int px = rivalPlayer.getX();
            double m = ((double) (y) - py) / (x - px);
            System.out.printf("x: %d, px: %d | y: %d, py: %d, m = %f\n", x, px, y, py, m);
            System.out.printf("FIELD_SIZE_X: %d, BORDER_WIDTH: %d\n", Constants.FIELD_SIZE_X, Constants.BORDER_WIDTH+200);
            animationSystem.rivalShoot(m,Constants.FIELD_SIZE_X,Constants.BORDER_WIDTH+200,gameModel.getHeight(),gameModel.getRivalPlayer().getColor());
        }

    }

    public void changeColor() {
        Ball player = gameModel.getPlayer();
        player.changeColor();
        gameModel.getView().repaint();
    }
}