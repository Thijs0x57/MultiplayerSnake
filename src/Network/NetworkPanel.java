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

    }

    public void joinGame(String address, int port) throws IOException {

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
