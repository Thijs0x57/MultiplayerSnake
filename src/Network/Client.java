package Network;

import Network.Callbacks.*;

import java.awt.*;
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

    private KeyPressReceived _keyPressReceived;
    private PositionChangedReceived _positionChangedReceived;

    private AppleRemoveReceived _appleRemoveReceived;
    private AppleSpawnReceived _appleSpawnReceived;

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
                        case KEYS:
                            _keyPressReceived.onKeyPressReceived(_clientDataInputStream.readInt());
                            break;
                        case POSITION:
                            String positionData = _clientDataInputStream.readUTF();
                            int posX = Integer.parseInt(positionData.split(":")[0]);
                            int posY = Integer.parseInt(positionData.split(":")[1]);
                            _positionChangedReceived.onPositionChanged(new Point(posX, posY));
                            break;
                        case APPLE_SPAWN:
                            String appleData = _clientDataInputStream.readUTF();
                            int appX = Integer.parseInt(appleData.split(":")[0]);
                            int appY = Integer.parseInt(appleData.split(":")[1]);
                            _appleSpawnReceived.onAppleSpawnReceived(appX, appY);
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
