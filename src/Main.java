import game.GamePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by thijs on 15-May-17.
 */
public class Main
{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Multiplayer Snake");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new GamePanel());
        frame.setMinimumSize(new Dimension(800, 608));
        frame.setMaximumSize(new Dimension(800, 608));
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
    }
}
