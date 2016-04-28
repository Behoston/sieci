package tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientConnection extends Thread {
    private Socket connectionSocket;
    private Integer clientId;
    private MessageSource messageSource;
    private ServerListener listener;

    ClientConnection(Socket connectionSocket, ClientIdSource clientIdSource, MessageSource messageSource) {
        this.connectionSocket = connectionSocket;
        this.clientId = clientIdSource.giveMeId();
        this.messageSource = messageSource;
        listener = new ServerListener(messageSource, clientId, connectionSocket);

    }

    public void run() {
        System.out.println("##########################");
        System.out.println("Client connection started");
        System.out.println("Remote IP: " + connectionSocket.getInetAddress().getHostAddress());
        System.out.println("Client id: " + clientId.toString());
        listener.start();
        try {
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            outToClient.writeBytes("Hello, your ID is: " + clientId + "; To send message please use 3 first chars to set target");
        } catch (IOException ignored) {
        }
        try {
            String clientSentence;
            while (true) {
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                clientSentence = inFromClient.readLine();
                System.out.println("###########################");
                System.out.println("[" + clientId.toString() + "]Received: " + clientSentence);
                if (clientSentence != null) {
                    if (clientSentence.toUpperCase().equals("KONIEC\n")) {
                        System.out.println("Koniec połączenia klienta o numerze" + this.clientId.toString());
                        break;
                    }
                }
                Integer to = Integer.parseInt(clientSentence.substring(0, 3));
                String message = clientSentence.substring(3);
                messageSource.putMessage(new Message(to, this.clientId, message));
            }
            connectionSocket.close();
        } catch (Exception ignored) {
        }
    }


}
