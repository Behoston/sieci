package yeti.server;


import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String argv[]) throws Exception {
        Yeti yeti = new Yeti();
        yeti.start();
        ServerSocket welcomeSocket = new ServerSocket(850);
        //noinspection InfiniteLoopStatement
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new ClientConnection(connectionSocket, yeti).start();
        }

    }
}
