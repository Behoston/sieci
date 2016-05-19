package tcp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientListener extends Thread {
    private Socket clientSocket;
    private Boolean connected;


    ClientListener(Socket clientSocket) {
        this.clientSocket = clientSocket;
        connected = clientSocket.isConnected();
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String response;
                response = inFromServer.readLine();
                if (response.contains("/END")) {
                    connected = Boolean.FALSE;
                    break;
                }
                System.out.println(response);
            } catch (Exception e) {
                connected = Boolean.FALSE;
                break;
            }
        }
        System.out.println("<Press Enter>");
    }

    Boolean isConnected() {
        return connected;
    }
}
