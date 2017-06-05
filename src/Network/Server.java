package Network;

import Network.Callbacks.MessageReceived;

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

    private MessageReceived _messageReceived;

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
                        case ChatMessage:
                            _messageReceived.onMessageReceived(_serverDataInputStream.readUTF());
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

    public void send(MessageType messageType, Object data) throws IOException {
        switch (messageType) {
            case ChatMessage:
                sendMessage(messageType, (String)data);
        }
        _serverDataOutputStream.flush();
    }

    public InetAddress getHost() {
        if(_serverSocket != null) {
            return _serverSocket.getInetAddress();
        }

        return null;
    }

    public void onMessageReceived(MessageReceived messageReceived) {
        _messageReceived = messageReceived;
    }
}
