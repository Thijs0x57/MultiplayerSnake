package game;

import Network.Client;
import Network.Server;
import game.objects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Michel on 5-6-2017.
 */
public class GamePanel extends JPanel implements ActionListener{
    private CopyOnWriteArrayList<GameObject> _gameObjects;

    private Player _player;
    private Player _enemyPlayer;

    private Snake _enemySnake;
    private Snake _snake;

    private Server _server;
    private Client _client;

    private Timer _gameLoopTimer;

    private int _spawnAppleCounter;

    long lastLoopTime = System.currentTimeMillis();

    public GamePanel() {
        setBackground(Color.black);
        setFocusable(true);

        _gameObjects = new CopyOnWriteArrayList<>();

        _player = new Player();
        _enemyPlayer = new Player();
        _enemyPlayer.getSnake().setDirection(Direction.LEFT);

        _gameObjects.add(_player.getSnake());
        _gameObjects.add(_enemyPlayer.getSnake());

        addKeyListener(new GameKeyAdapter(_player));

        _spawnAppleCounter = 0;

        // Game speed
        _gameLoopTimer = new Timer(1000 / GameConstants.GAME_SPEED, this);
        _gameLoopTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        Iterator<GameObject> it = _gameObjects.iterator();
        for(int i = 0; i < _gameObjects.size(); i++) {
            if(it.hasNext()) {
                GameObject go = it.next();

                if(go instanceof Snake) {
                    Snake snake = (Snake)go;

                    if (snake.getPosition().x < 0) {
                        snake.setPosition(new Point(getWidth(), snake.getPosition().y));
                    } else if (snake.getPosition().x > getWidth()) {
                        snake.setPosition(new Point(0, snake.getPosition().y));
                    } else if (snake.getPosition().y < 0) {
                        snake.setPosition(new Point(snake.getPosition().x, getHeight()));
                    } else if (snake.getPosition().y > getHeight()) {
                        snake.setPosition(new Point(snake.getPosition().x, 0));
                    }
                } else if(go instanceof Apple) {
                    Apple apple = (Apple)go;
                }

                go.draw(g2d);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    public synchronized void update() {
        if(_spawnAppleCounter < GameConstants.APPLE_SPAWN_RATE) {
            _spawnAppleCounter++;
        } else {
            // https://stackoverflow.com/a/363692/3677161
            int x = ThreadLocalRandom.current().nextInt(0, getWidth() + 1);
            int y = ThreadLocalRandom.current().nextInt(0, getHeight() + 1);

            _gameObjects.add(new Apple(new Point(x, y)));
            _spawnAppleCounter = 0;
        }

        // Collision checking
        Iterator<GameObject> itcc = _gameObjects.iterator();
        for(int i = 0; i < _gameObjects.size(); i++) {
            if(itcc.hasNext()) {
                GameObject go = itcc.next();

                if(go instanceof Snake) {
                    checkCollision(go);
                }
            }
        }

        // Update game objects
        Iterator<GameObject> itup = _gameObjects.iterator();
        for(int i = 0; i < _gameObjects.size(); i++) {
            if(itup.hasNext()) {
                itup.next().update();
            }
        }
    }

    public void checkCollision(GameObject gameObject) {
        Iterator<GameObject> itgo = _gameObjects.iterator();
        for (int i = 0; i < _gameObjects.size(); i++) {
            if (itgo.hasNext()) {
                GameObject go = itgo.next();

                if(gameObject instanceof Snake && go instanceof Apple) {
                    if (gameObject.hasCollision(go)) {
                        _gameObjects.remove(go);
                        ((Snake) gameObject).addBodyElement();
                    }
                } else if(gameObject instanceof Snake && go instanceof Snake) {
                    if(gameObject.hasCollision(go)) {
                        ((Snake)gameObject).removeBodyElement();
                    }
                }
            }
        }
    }

    public Player get_player()
    {
        return _player;
    }

    public Player get_enemyPlayer()
    {
        return _enemyPlayer;
    }

    public Snake get_enemySnake()
    {
        return _enemySnake;
    }

    public Snake get_snake()
    {
        return _snake;
    }

    public Server get_server()
    {
        return _server;
    }

    public Client get_client()
    {
        return _client;
    }
}
