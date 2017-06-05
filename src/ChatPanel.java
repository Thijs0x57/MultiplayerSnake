import Network.MessageType;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by thijs on 15-May-17.
 */
public class ChatPanel extends JPanel
{
    private JTextArea _txtareaChatArea;
    private JTextField _txtfldMessageField;
    private JButton _bttnSendMessage;

    private static final String EOF = "/r/n";

    private GridBagConstraints _gridBagContraints;



    public ChatPanel()
    {


        setSize(1000, 500);

        setLayout(new GridBagLayout());

        _gridBagContraints = new GridBagConstraints();
        _gridBagContraints.fill = GridBagConstraints.NONE;
        _gridBagContraints.gridx = 0;
        _gridBagContraints.gridy = 0;
        _gridBagContraints.ipadx = 250;
        _gridBagContraints.ipady = 400;
        _txtareaChatArea = new JTextArea();
        _txtareaChatArea.setEditable(false);
        _txtareaChatArea.setMaximumSize(new Dimension(250, 400));

        // HACKY CODE om JTextArea een maximum groote te geven
        // https://stackoverflow.com/questions/15161332/setting-up-a-maximum-component-size-when-using-gridbaglayout-in-java
        JScrollPane scrollPane = new JScrollPane(_txtareaChatArea);
        scrollPane.setMinimumSize(new Dimension(250, 400));
        JPanel wrappingPanel = new JPanel(null);
        wrappingPanel.setLayout(new BoxLayout(wrappingPanel, BoxLayout.LINE_AXIS));
        wrappingPanel.add(scrollPane);
        _gridBagContraints.weightx = 1.0;
        _gridBagContraints.fill = GridBagConstraints.BOTH;


        add(wrappingPanel, _gridBagContraints);

        _gridBagContraints.gridx = 0;
        _gridBagContraints.gridy = 1;
        _gridBagContraints.ipadx = 250;
        _gridBagContraints.ipady = 30;
        add(_txtfldMessageField = new JTextField(), _gridBagContraints);
        _txtfldMessageField.addActionListener(e -> {
            if(_txtfldMessageField.isFocusOwner()) {
                // Press Enter
            }
        });

        _gridBagContraints.gridx = 0;
        _gridBagContraints.gridy = 2;
        _gridBagContraints.ipadx = 250;
        _gridBagContraints.ipady = 30;
        _bttnSendMessage = new JButton("Send");
        _bttnSendMessage.addActionListener(e -> {
//            try {
//                if (_txtfldMessageField.getText().length() > 0) {
//                    addUserMessage(_txtfldMessageField.getText());
//
//                    if (_gameFrame.getNetworkPanel().isClient()) {
//                        _gameFrame.getNetworkPanel().getCheckersClient().send(MessageType.ChatMessage, _txtfldMessageField.getText());
//                    }
//
//                    if(_gameFrame.getNetworkPanel().isHost()) {
//                        _gameFrame.getNetworkPanel().getCheckerServer().send(MessageType.ChatMessage, _txtfldMessageField.getText());
//                    }
//
//                    _txtfldMessageField.setText("");
//                }
//            }catch (IOException e1) {
//                e1.printStackTrace();
//            }
        });
        add(_bttnSendMessage, _gridBagContraints);
    }

    public void clearChat() {
        _txtareaChatArea.setText("");
    }

    public void addSystemMessage(String message) {
        _txtareaChatArea.append("SYSTEM: " + message + "\n");
    }

    public void addUserMessage(String message) {
        _txtareaChatArea.append("YOU: " + message + "\n");
    }
    public void addOtherUserMessage(String message) {
        _txtareaChatArea.append("SOMEONE ELSE: " + message + "\n");
    }
}
