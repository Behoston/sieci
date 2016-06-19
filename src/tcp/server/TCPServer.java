package tcp.server;

import java.net.ServerSocket;
import java.net.Socket;

class TCPServer {
    public static void main(String argv[]) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(6789);
        MessageRouter messageRouter = new MessageRouter();
        //noinspection InfiniteLoopStatement
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            ClientConnection clientConnection = new ClientConnection(connectionSocket, messageRouter);
            messageRouter.connectClient(clientConnection);
            clientConnection.start();
        }

    }
}