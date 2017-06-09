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
public class GamePanel extends JPanel implements ActionListener {
    private CopyOnWriteArrayList<GameObject> _gameObjects;

    private Player _player;
    private Player _enemyPlayer;

    private Server _snakeServer;
    private Client _snakeClient;

    private boolean _isHost;
    private boolean _isClient;

    private Timer _gameLoopTimer;

    private int _spawnAppleCounter;

    private boolean _isRunning;

    public GamePanel(String[] startupArgs) {
        setBackground(Color.black);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Snake snake = _player.getSnake();

                if (snake != null) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            snake.setDirection(Direction.UP);
                            break;
                        case KeyEvent.VK_DOWN:
                            snake.setDirection(Direction.DOWN);
                            break;
                        case KeyEvent.VK_LEFT:
                            snake.setDirection(Direction.LEFT);
                            break;
                        case KeyEvent.VK_RIGHT:
                            snake.setDirection(Direction.RIGHT);
                            break;
                        case KeyEvent.VK_NUM_LOCK:
                            _player.getSnake().addBodyElement();
                    }
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        initGame();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Iterator<GameObject> it = _gameObjects.iterator();
        for (int i = 0; i < _gameObjects.size(); i++) {
            if (it.hasNext()) {
                GameObject go = it.next();

                if (go instanceof Snake) {
                    Snake snake = (Snake) go;

                    if (snake.getPosition().x < 0) {
                        snake.setPosition(new Point(getWidth(), snake.getPosition().y));
                    } else if (snake.getPosition().x > getWidth()) {
                        snake.setPosition(new Point(0, snake.getPosition().y));
                    } else if (snake.getPosition().y < 0) {
                        snake.setPosition(new Point(snake.getPosition().x, getHeight()));
                    } else if (snake.getPosition().y > getHeight()) {
                        snake.setPosition(new Point(snake.getPosition().x, 0));
                    }
                } else if (go instanceof Apple) {
                    Apple apple = (Apple) go;
                }

                go.draw(g2d);
            }
        }

        g2d.setColor(Color.white);
        g2d.drawString("Your Lives: " + _player.getSnake().getBodyCount(), 10, 10);
        g2d.drawString("Other Lives: " + _enemyPlayer.getSnake().getBodyCount(), 10, 20);
        if(_isHost)
            g2d.drawString("SERVER", 10, 30);
        else
            g2d.drawString("CLIENT", 10, 30);
    }

    long lastTime = System.currentTimeMillis();

    @Override
    public void actionPerformed(ActionEvent e) {
        long time = System.currentTimeMillis();
        if (time - lastTime > 1000) {
            lastTime = time;  // we're too far behind, catch up
        }
        int updatesNeeded = (int) ((time - lastTime) / GameConstants.GAME_UPDATE_INTERVAL);
        for (int i = 0; i < updatesNeeded; i++) {
            update();
            lastTime += GameConstants.GAME_UPDATE_INTERVAL;
        }
        repaint();
        // ... sleep until next frame ...
    }

    public synchronized void update() {
        try {
            System.out.println("Game won/lost/draw check");
            if (_player.getSnake().compareTo(_enemyPlayer.getSnake()) > 0) {
                // Player 1 win
            } else if (_enemyPlayer.getSnake().compareTo(_player.getSnake()) < 0) {
                // Player 2 win
            } else {
                // Draw
            }


            if (_isHost) {
                System.out.println("Apple spawn check");
                if (_spawnAppleCounter < GameConstants.APPLE_SPAWN_RATE) {
                    _spawnAppleCounter++;
                } else {
                    // https://stackoverflow.com/a/363692/3677161
                    int x = ThreadLocalRandom.current().nextInt(0, getWidth() + 1);
                    int y = ThreadLocalRandom.current().nextInt(0, getHeight() + 1);

                    _gameObjects.add(new Apple(new Point(x, y)));
                    _spawnAppleCounter = 0;

                    _snakeServer.sendMessage(MessageType.APPLE_SPAWN, x + ":" + y);
                }
            }

            System.out.println("Collision checking check");
            // Collision checking
            Iterator<GameObject> itcc = _gameObjects.iterator();
            for (int i = 0; i < _gameObjects.size(); i++) {
                if (itcc.hasNext()) {
                    GameObject go = itcc.next();

                    if(go instanceof Snake)
                        checkCollision(go);
                }
            }

            if (_isClient && _snakeClient != null && _snakeClient.getReadStream() != null) {
                System.out.println("Send snake position to client check");
                _snakeClient.sendMessage(MessageType.POSITION, _player.getSnake().getPosition().x + ":" + _player.getSnake().getPosition().y);
                System.out.println("Handle incoming server messages check");
                handleIncomingServerMessages();
            }


            if (_isHost && _snakeServer != null && _snakeServer.getReadStream() != null) {
                System.out.println("Send snake position to server check");
                _snakeServer.sendMessage(MessageType.POSITION, _player.getSnake().getPosition().x + ":" + _player.getSnake().getPosition().y);
                System.out.println("Handle incoming client messages check");
                handleIncomingClientMessages();
            }



            System.out.println("Update game objects check");
            // Update game objects
            Iterator<GameObject> itup = _gameObjects.iterator();
            for (int i = 0; i < _gameObjects.size(); i++) {
                if (itup.hasNext()) {
                    itup.next().update();
                }
            }
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

        _isHost = true;

        System.out.println("SERVER");
    }

    public void joinGame(int port, String address) throws IOException {
        _snakeClient = new Client(address, port);
        _snakeClient.start();

        _isClient = true;

        System.out.println("CLIENT");
    }

    public void initGame() {
        _gameObjects = new CopyOnWriteArrayList<>();

        Point hostSpawn = new Point(40, 17);
        Point clientSpawn = new Point(9, 17);

        if (_isHost) {
            _player = new Player(hostSpawn);
            _enemyPlayer = new Player(clientSpawn);
        }

        if (_isClient) {
            _player = new Player(clientSpawn);
            _enemyPlayer = new Player(hostSpawn);
        }

        _gameObjects.add(_player.getSnake());
        _gameObjects.add(_enemyPlayer.getSnake());

        _spawnAppleCounter = 0;

        // Game speed
        _gameLoopTimer = new Timer(1000 / 60, this);
        _gameLoopTimer.start();

        //gameLoop();
    }

    private void handleIncomingClientMessages() throws IOException {
        MessageType messageType = MessageType.values()[_snakeServer.getReadStream().read()];
        switch (messageType) {
            case KEYS:
                _enemyPlayer.getSnake().setDirection(Direction.values()[_snakeServer.getReadStream().readInt()]);
                break;
            case POSITION:
                String data = _snakeServer.getReadStream().readUTF();
                int x = Integer.parseInt(data.split(":")[0]);
                int y = Integer.parseInt(data.split(":")[1]);
                _enemyPlayer.getSnake().setPosition(new Point(x, y));
                break;
        }
    }

    private void handleIncomingServerMessages() throws IOException {
        MessageType messageType = MessageType.values()[_snakeClient.getReadStream().read()];
        switch (messageType) {
            case KEYS:
                _enemyPlayer.getSnake().setDirection(Direction.values()[_snakeClient.getReadStream().readInt()]);
                break;
            case POSITION:
                String data = _snakeClient.getReadStream().readUTF();
                int x = Integer.parseInt(data.split(":")[0]);
                int y = Integer.parseInt(data.split(":")[1]);
                _enemyPlayer.getSnake().setPosition(new Point(x, y));
                break;
            case APPLE_SPAWN:
                String appleData = _snakeClient.getReadStream().readUTF();
                int appX = Integer.parseInt(appleData.split(":")[0]);
                int appY = Integer.parseInt(appleData.split(":")[1]);
                _gameObjects.add(new Apple(new Point(appX, appY)));
                break;
        }
    }
}
