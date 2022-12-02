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
        /*
        val player = gameModel.getPlayer1();
        if (isInAnimation() || player == null) {
            return;
        }
        double m = ((double) (y) - player.getY()) / (x - player.getX());
        //send "pressed" signal to controller
        gameModel.getPlayer1().setSlope(m<0?m:m*-1);
        gameModel.getPlayer1().setActivated(true);

        activeObject.dispatch(new PlayerMoved(gameModel,m>0?-1:1, this));
        setInAnimation(false);
         */
        if(isPlayer)
        {
            Ball player = gameModel.getPlayer();
            double m = ((double) (y) - player.getY()) / (x - player.getX());
            animationSystem.playerShoot(m,Constants.BORDER_WIDTH,gameModel.getHeight(),player.getColor());
        }
        else
        {
            Ball rivalPlayer = gameModel.getRivalPlayer();
            double m = ((double) (y) - rivalPlayer.getY()) / (x - rivalPlayer.getX());
            animationSystem.rivalShoot(m,Constants.BORDER_WIDTH,gameModel.getHeight(),rivalPlayer.getColor());
        }

    }

    public void changeColor() {
        Ball player = gameModel.getPlayer();
        player.changeColor();
        gameModel.getView().repaint();
    }
}