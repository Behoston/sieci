package tcp.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientConnection extends Thread {
    private Socket connectionSocket;
    private MessageRouter messageRouter;

    ClientConnection(Socket connectionSocket, MessageRouter messageRouter) {
        this.connectionSocket = connectionSocket;
        this.messageRouter = messageRouter;

    }


    void sendToMyClient(String message, String from, Boolean priv) {
        String toClient = "";
        if (priv) {
            toClient += "*";
        }
        toClient += "[" + from + "]";
        toClient += message;
        toClient += "\n";
        try {
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            outToClient.writeBytes(toClient);
        } catch (IOException e) {
            messageRouter.disconnectClient(this);
            try {
                connectionSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    public void run() {
        System.out.println("##########################");
        System.out.println("Client connection started");
        System.out.println("Remote IP: " + connectionSocket.getInetAddress().getHostAddress());
        try {
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            outToClient.writeBytes("Hello, your IP is: " + connectionSocket.getInetAddress().getHostAddress() + "\n" +
                    "To send private message please use @IP@message to set target\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String message;
            while (true) {
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                message = inFromClient.readLine();
                if (message != null) {
                    System.out.println("###########################");
                    System.out.println("[" + connectionSocket.getInetAddress().getHostAddress() + "]Received: " + message);
                    messageRouter.routeMessage(message, connectionSocket.getInetAddress().getHostAddress());
                    if (message.toUpperCase().contains("/END")) {
                        System.out.println("[" + connectionSocket.getInetAddress().getHostAddress() + "]Connection closed");
                        break;
                    }
                } else {
                    break;
                }
            }
            connectionSocket.close();
        } catch (Exception ignored) {
        }
        messageRouter.disconnectClient(this);

    }


    String getIp() {
        return connectionSocket.getInetAddress().getHostAddress();
    }
}
