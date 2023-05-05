package org.ort_rehovot.bubble_shooter;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class MenuPanel extends JPanel {

    public MenuPanel() throws IOException {
        //hideMouseCursor();
        ResourceLoader.init();
        Button b = new Button("Click Here");

    }

    @Override
    public void paintComponent(Graphics g) {
        //setPauseVisible(GlobalState.getInstance().isPaused());
        super.paintComponent(g);
        g.drawImage(ResourceLoader.getInstance().getBgImage(), 0, 0, getWidth(), getHeight(), null);
        Image image = ResourceLoader.getInstance().getLogo();
        g.drawImage(image, Constants.BORDER_X-350, 0
                , 800, 200, null);
    }
}
