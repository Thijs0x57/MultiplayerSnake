package Network;

import Network.Callbacks.*;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Michel on 22-5-2017.
 */
public class Server {
    private ServerSocket _serverSocket;

    private DataInputStream _serverDataInputStream;
    private DataOutputStream _serverDataOutputStream;

    private ArrayList<Socket> _connectedSockets;

    private boolean _isRunning;

    private int _serverPort;

    private KeyPressReceived _keyPressReceived;
    private PositionChangedReceived _positionChangedReceived;

    private AppleRemoveReceived _appleRemoveReceived;
    private AppleSpawnReceived _appleSpawnReceived;

    public int maxConnections = 1;

    public Server(int port) throws IOException {
        _serverPort = port;

        _isRunning = false;
    }

    public void start()  {
        Runnable serverTask = () -> {
            try {
                _serverSocket = new ServerSocket(_serverPort);
                _connectedSockets = new ArrayList<>();

                System.out.println("Wating for connections...");
                while(_connectedSockets.size() < maxConnections) {
                    Socket newConnection = _serverSocket.accept();

                    System.out.println("New connection from: " + newConnection.getInetAddress().getHostAddress());

                    _serverDataInputStream = new DataInputStream(new BufferedInputStream(newConnection.getInputStream()));
                    _serverDataOutputStream = new DataOutputStream(new BufferedOutputStream(newConnection.getOutputStream()));

                    _connectedSockets.add(newConnection);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }

    public DataInputStream getReadStream() {
        return _serverDataInputStream;
    }

    public void sendMessage(MessageType messageType, String message) throws IOException {
        if(_serverDataOutputStream != null) {
            _serverDataOutputStream.writeByte(messageType.ordinal());
            _serverDataOutputStream.writeUTF(message);
            _serverDataOutputStream.flush();
        }
    }

    public void sendMessageInt(MessageType messageType, int message) throws IOException{
        if(_serverDataOutputStream != null) {
            _serverDataOutputStream.writeByte(messageType.ordinal());
            _serverDataOutputStream.writeInt(message);
            _serverDataOutputStream.flush();
        }
    }

    public void send(MessageType messageType, Object data) throws IOException {
        switch (messageType) {
            case KEYS:
                sendMessageInt(messageType, (int)data);
                break;
            case POSITION:
                sendMessage(messageType, (String)data);
                break;
        }
    }

    public InetAddress getHost() {
        if(_serverSocket != null) {
            return _serverSocket.getInetAddress();
        }

        return null;
    }

    public void onAppleSpawnReceived(AppleSpawnReceived appleSpawnReceived)
    {
        _appleSpawnReceived = appleSpawnReceived;
    }

    public void onAppleRemoveReceived(AppleRemoveReceived appleRemoveReceived)
    {
        _appleRemoveReceived = appleRemoveReceived;
    }

    public void onKeyPressReceived(KeyPressReceived keyPressReceived)
    {
        _keyPressReceived = keyPressReceived;
    }

    public void onPositionChanged(PositionChangedReceived positionChangedReceived) {
        _positionChangedReceived = positionChangedReceived;
    }
}
