package tcp;

import java.net.*;

class TCPServer {
    public static void main(String argv[]) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(6789);
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            ClientIdSource clientIdSource = new ClientIdSource();
            MessageSource messageSource = new MessageSource();
            ClientConnection clientConnection = new ClientConnection(connectionSocket, clientIdSource, messageSource);
            clientConnection.start();
        }

    }
}