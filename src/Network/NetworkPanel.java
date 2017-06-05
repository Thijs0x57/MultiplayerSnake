package Network;

import game.GamePanel;
import game.objects.Direction;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by thijs on 05-Jun-17.
 */
public class NetworkPanel
{
    private Server _checkersServer;
    private Client _checkersClient;

    private GamePanel _gamePanel;

    private boolean _isHost;
    private boolean _isClient;

    public NetworkPanel()
    {
        _isHost = false;
        _isClient = false;
    }

    public void hostGame(int port) throws IOException
    {
        _checkersServer = new Server(port);
        _checkersServer.start();
        _checkersServer.onKeyPressReceived((key) -> {

            switch (key)
            {
                case 1:
                    _gamePanel.get_enemyPlayer().getSnake().setDirection(Direction.UP);
                    break;
                case 2:
                    _gamePanel.get_enemyPlayer().getSnake().setDirection(Direction.RIGHT);
                    break;
                case 3:
                    _gamePanel.get_enemyPlayer().getSnake().setDirection(Direction.DOWN);
                    break;
                case 4:
                    _gamePanel.get_enemyPlayer().getSnake().setDirection(Direction.LEFT);
                    break;
            }
        });

        _isHost = true;
    }

    public void joinGame(String address, int port) throws IOException {
        _checkersClient = new Client(address, port);
        _checkersClient.start();
        _checkersClient.onKeyPressReceived((key) -> {

            switch (key)
            {
                case 1:
                    _gamePanel.get_enemyPlayer().getSnake().setDirection(Direction.UP);
                    break;
                case 2:
                    _gamePanel.get_enemyPlayer().getSnake().setDirection(Direction.RIGHT);
                    break;
                case 3:
                    _gamePanel.get_enemyPlayer().getSnake().setDirection(Direction.DOWN);
                    break;
                case 4:
                    _gamePanel.get_enemyPlayer().getSnake().setDirection(Direction.LEFT);
                    break;
            }
        });

        _isClient = true;
    }

    public Server get_checkersServer()
    {
        return _checkersServer;
    }

    public Client get_checkersClient()
    {
        return _checkersClient;
    }
}
