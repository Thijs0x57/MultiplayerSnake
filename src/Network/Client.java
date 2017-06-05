package Network;

import Network.Callbacks.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.Key;

/**
 * Created by Michel on 22-5-2017.
 */
public class Client {
    private Socket _clientSocket;

    private DataInputStream _clientDataInputStream;
    private DataOutputStream _clientDataOutputStream;

    private AppleRemoveReceived _appleRemoveReceived;
    private AppleSpawnReceived _appleSpawnReceived;
    private KeyPressReceived _keyPressReceived;
    private MessageReceived _messageReceived;
    private SnakeLengthReceived _snakeLengthReceived;



    public Client(String host, int port) throws IOException {
        _clientSocket = new Socket(host, port);

        _clientDataOutputStream = new DataOutputStream(_clientSocket.getOutputStream());
        _clientDataInputStream = new DataInputStream(_clientSocket.getInputStream());
    }

    public void start() {
        Runnable clientTask = () -> {
            try {
                while(true) {
                    MessageType messageType = MessageType.values()[_clientDataInputStream.read()];

                    switch (messageType) {
                        // text chat message
                        case CHATMESSAGE:
                            _messageReceived.onMessageReceived(_clientDataInputStream.readUTF());
                            break;
                        case SNAKE_LENGTH:
                            _snakeLengthReceived.onSnakeLengthReceived(_clientDataInputStream.readInt());
                            break;
                        case APPLE_SPAWN:
                            _appleSpawnReceived.onAppleSpawnReceived(_clientDataInputStream.readInt(), _clientDataInputStream.readInt());
                            break;
                        case APPLE_REMOVE:
                            _appleRemoveReceived.onAppleRemoveReceived(_clientDataInputStream.readInt(), _clientDataInputStream.readInt());
                            break;
                        case KEYS:
                            _keyPressReceived.onKeyPressReceived(_clientDataInputStream.readInt());
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread clientThread = new Thread(clientTask);
        clientThread.start();
    }

    public void sendMessageString(MessageType messageType, String message) throws IOException {
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
            case CHATMESSAGE:
                sendMessageString(messageType, (String)data);
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
        _clientDataOutputStream.flush();
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
