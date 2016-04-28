package tcp.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class TCPClient {

    public static void main(String argv[]) throws Exception {
        String ja = "localhost";
        String request;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket(ja, 6789);
        ClientListener listener = new ClientListener(clientSocket);
        listener.start();
        while (true) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            //czytanie
            request = inFromUser.readLine();
            outToServer.writeBytes(request + '\n');
            if (!listener.isConnected()) {
                System.out.println("Połączenie zakończone");
                break;
            }
        }
        clientSocket.close();
    }

}