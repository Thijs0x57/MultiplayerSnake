package game;

import Network.Client;
import Network.MessageType;
import Network.Server;
import game.objects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Michel on 5-6-2017.
 */
public class GamePanel extends JPanel implements ActionListener{
    private CopyOnWriteArrayList<GameObject> _gameObjects;

    private Player _player;
    private Player _enemyPlayer;

    private Server _snakeServer;
    private Client _snakeClient;

    private boolean _isHost;
    private boolean _isClient;

    private Timer _gameLoopTimer;

    private int _spawnAppleCounter;

    long lastLoopTime = System.currentTimeMillis();

    public GamePanel(String[] startupArgs) {
        setBackground(Color.black);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    Snake snake = _player.getSnake();

                    if (snake != null) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_UP:
                                snake.setDirection(Direction.UP);

                                if (_isHost) {
                                    _snakeServer.sendMessageInt(MessageType.KEYS, Direction.UP.ordinal());
                                    _snakeServer.sendMessage(MessageType.POSITION, snake.getPosition().x + ":" + snake.getPosition().y);
                                } else if (_isClient) {
                                    _snakeClient.sendMessageInt(MessageType.KEYS, Direction.UP.ordinal());
                                    _snakeClient.sendMessage(MessageType.POSITION, snake.getPosition().x + ":" + snake.getPosition().y);
                                }
                                break;
                            case KeyEvent.VK_DOWN:
                                snake.setDirection(Direction.DOWN);

                                if (_isHost) {
                                    _snakeServer.sendMessageInt(MessageType.KEYS, Direction.DOWN.ordinal());
                                    _snakeServer.sendMessage(MessageType.POSITION, snake.getPosition().x + ":" + snake.getPosition().y);
                                } else if (_isClient) {
                                    _snakeClient.sendMessageInt(MessageType.KEYS, Direction.DOWN.ordinal());
                                    _snakeClient.sendMessage(MessageType.POSITION, snake.getPosition().x + ":" + snake.getPosition().y);
                                }
                                break;
                            case KeyEvent.VK_LEFT:
                                snake.setDirection(Direction.LEFT);

                                if (_isHost) {
                                    _snakeServer.sendMessageInt(MessageType.KEYS, Direction.LEFT.ordinal());
                                    _snakeServer.sendMessage(MessageType.POSITION, snake.getPosition().x + ":" + snake.getPosition().y);
                                } else if (_isClient) {
                                    _snakeClient.sendMessageInt(MessageType.KEYS, Direction.LEFT.ordinal());
                                    _snakeClient.sendMessage(MessageType.POSITION, snake.getPosition().x + ":" + snake.getPosition().y);
                                }
                                break;
                            case KeyEvent.VK_RIGHT:
                                snake.setDirection(Direction.RIGHT);

                                if (_isHost) {
                                    _snakeServer.sendMessageInt(MessageType.KEYS, Direction.RIGHT.ordinal());
                                    _snakeServer.sendMessage(MessageType.POSITION, snake.getPosition().x + ":" + snake.getPosition().y);
                                } else if (_isClient) {
                                    _snakeClient.sendMessageInt(MessageType.KEYS, Direction.RIGHT.ordinal());
                                    _snakeClient.sendMessage(MessageType.POSITION, snake.getPosition().x + ":" + snake.getPosition().y);
                                }
                                break;
                            case KeyEvent.VK_NUM_LOCK:
                                _player.getSnake().addBodyElement();
                        }
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        try {
            if (startupArgs.length > 0) {
                if (startupArgs[0].equals("client")) {
                    // 1st argument: address (String)
                    // 2nd argument: port (int)
                    joinGame(Integer.parseInt(startupArgs[1]), startupArgs[2]);
                }

                if (startupArgs[0].equals("server")) {
                    hostGame(Integer.parseInt(startupArgs[1]));
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        initGame();
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

        g2d.setColor(Color.white);
        g2d.drawString("Your Lives: " + _player.getSnake().getBodyCount(), 10, 10);
        g2d.drawString("Other Lives: " + _enemyPlayer.getSnake().getBodyCount(), 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    public synchronized void update() {
        if(_isHost) {
            if (_spawnAppleCounter < GameConstants.APPLE_SPAWN_RATE) {
                _spawnAppleCounter++;
            } else {
                // https://stackoverflow.com/a/363692/3677161
                int x = ThreadLocalRandom.current().nextInt(0, getWidth() + 1);
                int y = ThreadLocalRandom.current().nextInt(0, getHeight() + 1);

                _gameObjects.add(new Apple(new Point(x, y)));
                _spawnAppleCounter = 0;

                try {
                    _snakeServer.sendMessage(MessageType.APPLE_SPAWN, x + ":" + y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

        try {
        if(_isHost)
            if(_snakeServer != null && _player != null)
                _snakeServer.sendMessage(MessageType.POSITION, _player.getSnake().getPosition().x + ":" + _player.getSnake().getPosition().y);
        else if (_isClient)
            if(_snakeClient != null && _player != null)
                _snakeServer.sendMessage(MessageType.POSITION, _player.getSnake().getPosition().x + ":" + _player.getSnake().getPosition().y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkCollision(GameObject gameObject) {
        Iterator<GameObject> itgo = _gameObjects.iterator();
        for (int i = 0; i < _gameObjects.size(); i++) {
            if (itgo.hasNext()) {
                GameObject go = itgo.next();

                if (gameObject instanceof Snake && go instanceof Apple) {
                    if (gameObject.hasCollision(go)) {
                        _gameObjects.remove(go);
                        ((Snake) gameObject).addBodyElement();
                    }
                } else if (gameObject instanceof Snake && go instanceof Snake) {
                    if (gameObject.hasCollision(go)) {
                        if (!((Snake) gameObject).isProtected()) {
                            ((Snake) gameObject).removeBodyElement();
                            ((Snake) gameObject).setIsProtected();
                        }
                    }
                }
            }
        }
    }

    public void hostGame(int port) throws IOException {
        _snakeServer = new Server(port);
        _snakeServer.start();
        _snakeServer.onKeyPressReceived(key -> {
            Direction direction = Direction.values()[key];
            handleEnemyDirection(direction);
        });
        _snakeServer.onPositionChanged(position -> {
            _enemyPlayer.getSnake().setPosition(position);
        });

        _isHost = true;
    }

    public void joinGame(int port, String address) throws IOException {
        _snakeClient = new Client(address, port);
        _snakeClient.start();
        _snakeClient.onKeyPressReceived(key -> {
            Direction direction = Direction.values()[key];
            handleEnemyDirection(direction);
        });
        _snakeClient.onPositionChanged(position -> {
            _enemyPlayer.getSnake().setPosition(position);
        });
        _snakeClient.onAppleSpawned((x, y) -> {
            _gameObjects.add(new Apple(new Point(x, y)));
        });

        _isClient = true;
    }

    public void initGame() {
        _gameObjects = new CopyOnWriteArrayList<>();

        Point hostSpawn = new Point(40, 17);
        Point clientSpawn = new Point(9, 17);

        if(_isHost) {
            _player = new Player(hostSpawn);
            _enemyPlayer = new Player(clientSpawn);
        }

        if(_isClient) {
            _player = new Player(clientSpawn);
            _enemyPlayer = new Player(hostSpawn);
        }

        _gameObjects.add(_player.getSnake());
        _gameObjects.add(_enemyPlayer.getSnake());

        _spawnAppleCounter = 0;

        // Game speed
        _gameLoopTimer = new Timer(1000 / GameConstants.GAME_SPEED, this);
        _gameLoopTimer.start();
    }

    private void handleEnemyDirection(Direction direction) {
        switch (direction)
        {
            case UP:
                _enemyPlayer.getSnake().setDirection(direction);
                break;
            case DOWN:
                _enemyPlayer.getSnake().setDirection(direction);
                break;
            case LEFT:
                _enemyPlayer.getSnake().setDirection(direction);
                break;
            case RIGHT:
                _enemyPlayer.getSnake().setDirection(direction);
                break;
        }
    }
}
