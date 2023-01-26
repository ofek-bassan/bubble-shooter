package org.ort_rehovot.bubble_shooter;

import lombok.Getter;
import org.ort_rehovot.bubble_shooter.ipc.GameProtocol;
import org.ort_rehovot.bubble_shooter.ipc.Server;

import java.net.SocketException;


public class GameController {
    @Getter
    private final GameModel gameModel;
    private final AnimationSystem animationSystem;

    public GameController(GameModel gameModel, int port) throws SocketException {
        this.gameModel = gameModel;
        animationSystem = new AnimationSystem(gameModel);
        animationSystem.start();
        Server server;
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
     */
    public void shoot(double m,boolean isPlayer) {

        if(isPlayer)
        {
            m = Double.parseDouble(String.format("%.5f",m));
            //System.out.println("m:"+m);
            //System.out.println(Constants.PLAYER_X);
            //System.out.println(Constants.BORDER_X-Constants.PLAYER_X);
            //System.out.println(Constants.BORDER_X);
            animationSystem.playerShoot(m,Constants.BORDER_X,0,gameModel.getHeight(),gameModel.getPlayer().getColor());
        }
        else
        {
            //System.out.println("rival m:"+m);
            //System.out.println(Constants.RIVAL_X - Constants.RIVAL_BORDER_WIDTH);
            //System.out.println(Constants.FIELD_SIZE_X-Constants.RIVAL_X);
            //System.out.println(Constants.FIELD_SIZE_X-Constants.RIVAL_BORDER_WIDTH);
            animationSystem.rivalShoot(m,Constants.FIELD_SIZE_X,Constants.RIVAL_BORDER_WIDTH,gameModel.getHeight(),gameModel.getRivalPlayer().getColor());
        }

    }

    public void changeColor() {
        Ball player = gameModel.getPlayer();
        player.changeColor();
        gameModel.getView().repaint();
    }
}