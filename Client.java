import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField ipAddressField;
    private JTextField portField;
    private JButton connectButton;
    private JTextField messageField;
    private JButton sendButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Client client = new Client();
                    client.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Client() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        ipAddressField = new JTextField("127.0.0.1");
        ipAddressField.setBounds(10, 10, 120, 25);
        frame.getContentPane().add(ipAddressField);

        portField = new JTextField("12345");
        portField.setBounds(140, 10, 80, 25);
        frame.getContentPane().add(portField);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        connectButton.setBounds(240, 10, 80, 25);
        frame.getContentPane().add(connectButton);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 50, 460, 160);
        frame.getContentPane().add(scrollPane);

        messageField = new JTextField();
        messageField.setBounds(10, 220, 340, 25);
        frame.getContentPane().add(messageField);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        sendButton.setBounds(360, 220, 80, 25);
        frame.getContentPane().add(sendButton);
    }

    private void connectToServer() {
        String ipAddress = ipAddressField.getText();
        int port = Integer.parseInt(portField.getText());
        textArea.append("Connect to server...\n");

        new Thread(new Runnable() {
            public void run() {
                try (Socket socket = new Socket(ipAddress, port);
                     Scanner in = new Scanner(socket.getInputStream());
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    while (in.hasNextLine()) {
                        String message = in.nextLine();
                        textArea.append("Server: " + message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        textArea.append("Client: " + message + "\n");
        messageField.setText("");
    }
}
