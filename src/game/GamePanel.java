package game;

import Network.Callbacks.KeyPressReceived;
import Network.Client;
import Network.Server;
import game.objects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Michel on 5-6-2017.
 */
public class GamePanel extends JPanel implements ActionListener{
    private ArrayList<GameObject> _gameObjects;

    private Player _player;
    private Player _enemyPlayer;

    private Snake _enemySnake;
    private Snake _snake;

    private Server _server;
    private Client _client;

    private Timer _gameLoopTimer;

    long lastLoopTime = System.currentTimeMillis();

    public GamePanel() {
        setBackground(Color.black);
        setFocusable(true);

        _gameObjects = new ArrayList<>();

        _player = new Player();
        _enemyPlayer = new Player();

        _gameObjects.add(_player.getSnake());
        _gameObjects.add(_enemyPlayer.getSnake());

        addKeyListener(new GameKeyAdapter(_player));

        //control enemy snake
        setEnemySnakeDir();

        // Game speed
        _gameLoopTimer = new Timer(1000 / GameConstants.GAME_SPEED, this);
        _gameLoopTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Iterator<GameObject> it = _gameObjects.iterator();
        for(int i = 0; i < _gameObjects.size(); i++) {
            if(it.hasNext()) {
                it.next().draw((Graphics2D)g);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    public void update() {
        Iterator<GameObject> it = _gameObjects.iterator();
        for(int i = 0; i < _gameObjects.size(); i++) {
            if(it.hasNext()) {
                it.next().update();
            }
        }
    }


    public void setEnemySnakeDir()
    {
        //_server.onKeyPressReceived(key -> _enemySnake.setDirection(Direction.UP));
    }
}
