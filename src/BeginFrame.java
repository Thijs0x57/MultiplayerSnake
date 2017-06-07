import game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BeginFrame extends JFrame {


    public static void main(String[] args) {
        BeginFrame frameTabel = new BeginFrame();
    }

    JPanel panel = new JPanel();


    JButton joinGame = new JButton("Join Game");
    JButton hostGame = new JButton("Host game");

    BeginFrame(){
        super("Welcome");
        setSize(440,100);
        setLocation(500,280);
        panel.setLayout (null);

        joinGame.setBounds(0,0,200,40);
        hostGame.setBounds(210,0,200,40);

        panel.add(joinGame);
        panel.add(hostGame);

        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        actionlogin();
    }

    public void actionlogin()
    {
        joinGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String [] args = {"client", "53436", "127.0.0.1"};
                JFrame frame = new JFrame("Multiplayer Snake");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(new GamePanel(args));
                dispose();
                frame.setMinimumSize(new Dimension(800, 608));
                frame.setMaximumSize(new Dimension(800, 608));
                frame.setResizable(false);
                frame.pack();
                frame.setVisible(true);

            }
        });

        hostGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String [] args = {"server", "53436"};
                JFrame frame = new JFrame("Multiplayer Snake");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(new GamePanel(args));
                frame.setMinimumSize(new Dimension(800, 608));
                frame.setMaximumSize(new Dimension(800, 608));
                frame.setResizable(false);

                frame.pack();
                frame.setVisible(true);
                dispose();
            }
        });

    }
}