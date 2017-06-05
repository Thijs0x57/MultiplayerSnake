package Network;

import Network.Callbacks.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    private AppleRemoveReceived _appleRemoveReceived;
    private AppleSpawnReceived _appleSpawnReceived;
    private KeyPressReceived _keyPressReceived;
    private MessageReceived _messageReceived;
    private SnakeLengthReceived _snakeLengthReceived;

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

                    _serverDataInputStream = new DataInputStream(newConnection.getInputStream());
                    _serverDataOutputStream = new DataOutputStream(newConnection.getOutputStream());


                    _connectedSockets.add(newConnection);
                }

                while(true) {
                    MessageType messageType = MessageType.values()[_serverDataInputStream.read()];

                    switch(messageType) {
                        // text chat message
                        case CHATMESSAGE:
                            _messageReceived.onMessageReceived(_serverDataInputStream.readUTF());
                            break;
                        case SNAKE_LENGTH:
                            _snakeLengthReceived.onSnakeLengthReceived(_serverDataInputStream.readInt());
                            break;
                        case APPLE_SPAWN:
                            _appleSpawnReceived.onAppleSpawnReceived(_serverDataInputStream.readInt(), _serverDataInputStream.readInt());
                            break;
                        case APPLE_REMOVE:
                            _appleRemoveReceived.onAppleRemoveReceived(_serverDataInputStream.readInt(), _serverDataInputStream.readInt());
                            break;
                        case KEYS:
                            _keyPressReceived.onKeyPressReceived(_serverDataInputStream.readInt());
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }

    public void sendMessage(MessageType messageType, String message) throws IOException {
        _serverDataOutputStream.writeByte(messageType.ordinal());
        _serverDataOutputStream.writeUTF(message);
        _serverDataOutputStream.flush();
    }

    public void sendMessageInt(MessageType messageType, int message) throws IOException{
        _serverDataOutputStream.writeByte(messageType.ordinal());
        _serverDataOutputStream.writeInt(message);
        _serverDataOutputStream.flush();
    }

    public void send(MessageType messageType, Object data) throws IOException {
        switch (messageType) {
            case CHATMESSAGE:
                sendMessage(messageType, (String)data);
                break;
            case APPLE_SPAWN:
                sendMessageInt(messageType, (int)data);
                break;
            case APPLE_REMOVE:
                sendMessageInt(messageType, (int)data);
                break;
            case SNAKE_LENGTH:
                sendMessageInt(messageType, (int)data);
                break;
            case KEYS:
                sendMessageInt(messageType, (int)data);
                break;
        }
        _serverDataOutputStream.flush();
    }

    public InetAddress getHost() {
        if(_serverSocket != null) {
            return _serverSocket.getInetAddress();
        }

        return null;
    }

    public void onMessageReceived(MessageReceived messageReceived)
    {
        _messageReceived = messageReceived;
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

    public void onSnakeLengthReceived(SnakeLengthReceived snakeLengthReceived)
    {
        _snakeLengthReceived = snakeLengthReceived;
    }
}
