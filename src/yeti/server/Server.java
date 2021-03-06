package yeti.server;


import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String argv[]) throws Exception {
        Integer power = Integer.parseInt(argv[0]);
        Yeti yeti = new Yeti(power);
        yeti.start();
        ServerSocket welcomeSocket = new ServerSocket(6454);

        //noinspection InfiniteLoopStatement
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            new ClientConnection(connectionSocket, yeti).start();
        }

    }
}
