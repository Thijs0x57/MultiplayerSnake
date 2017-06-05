package Network;

import Network.Callbacks.MessageReceived;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Michel on 22-5-2017.
 */
public class Client {
    private Socket _clientSocket;

    private DataInputStream _clientDataInputStream;
    private DataOutputStream _clientDataOutputStream;

    private MessageReceived _messageReceived;

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
                        case ChatMessage:
                            _messageReceived.onMessageReceived(_clientDataInputStream.readUTF());
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

    public void send(MessageType messageType, Object data) throws IOException {
        switch (messageType) {
            case ChatMessage:
                sendMessage(messageType, (String)data);
        }
        _clientDataOutputStream.flush();
    }

    public void onMessageReceived(MessageReceived messageReceived) {
        _messageReceived = messageReceived;
    }
}
