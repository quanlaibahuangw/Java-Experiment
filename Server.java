import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField ipAddressField;
    private JTextField portField;
    private JButton startButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Server server = new Server();
                    server.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Server() {
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

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        startButton.setBounds(240, 10, 80, 25);
        frame.getContentPane().add(startButton);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 50, 460, 200);
        frame.getContentPane().add(scrollPane);
    }

    private void startServer() {
        int port = Integer.parseInt(portField.getText());
        textArea.append("Server starting...\n");

        new Thread(new Runnable() {
            public void run() {
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        textArea.append("Client connected...\n");

                        new Thread(new Runnable() {
                            public void run() {
                                try (Scanner in = new Scanner(clientSocket.getInputStream());
                                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                                    while (in.hasNextLine()) {
                                        String message = in.nextLine();
                                        textArea.append("Client: " + message + "\n");
                                        out.println("Server: " + message);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
