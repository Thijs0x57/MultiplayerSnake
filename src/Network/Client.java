package Network;

import Network.Callbacks.*;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.Key;

/**
 * Created by Michel on 22-5-2017.
 */
public class Client {
    private Socket _clientSocket;

    private DataInputStream _clientDataInputStream;
    private DataOutputStream _clientDataOutputStream;

    private KeyPressReceived _keyPressReceived;
    private PositionChangedReceived _positionChangedReceived;

    private AppleRemoveReceived _appleRemoveReceived;
    private AppleSpawnReceived _appleSpawnReceived;

    private MessageReceived _messageReceived;
    private SnakeLengthReceived _snakeLengthReceived;



    public Client(String host, int port) throws IOException {
        _clientSocket = new Socket(host, port);

        _clientDataOutputStream = new DataOutputStream(new BufferedOutputStream(_clientSocket.getOutputStream()));
        _clientDataInputStream = new DataInputStream(new BufferedInputStream(_clientSocket.getInputStream()));
    }

    public void start() {
        Runnable clientTask = () -> {

        };
        Thread clientThread = new Thread(clientTask);
        clientThread.start();
    }

    public DataInputStream getReadStream() {
        return _clientDataInputStream;
    }

    public void sendMessage(MessageType messageType, String message) throws IOException {
        _clientDataOutputStream.writeByte(messageType.ordinal());
        _clientDataOutputStream.writeUTF(message);
        _clientDataOutputStream.flush();
    }

    public void sendMessageInt(MessageType messageType, int message) throws IOException{
        _clientDataOutputStream.writeByte(messageType.ordinal());
        _clientDataOutputStream.writeInt(message);
        _clientDataOutputStream.flush();
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

    public void onMessageReceived(MessageReceived messageReceived)
    {
        _messageReceived = messageReceived;
    }

    public void onAppleSpawned(AppleSpawnReceived appleSpawnReceived)
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

    public void onSnakeLengthChanged(SnakeLengthReceived snakeLengthReceived)
    {
        _snakeLengthReceived = snakeLengthReceived;
    }

    public void onPositionChanged(PositionChangedReceived positionChangedReceived) {
        _positionChangedReceived = positionChangedReceived;
    }
}
