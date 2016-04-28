package tcp;

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
                String response;
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                response = inFromServer.readLine();
                System.out.println("FROM SERVER: " + response);
                if (response.equals("KONIEC")) {
                    connected = Boolean.FALSE;
                    break;
                }
            } catch (Exception ignored) {
            }
        }
    }

    Boolean isConnected() {
        return connected;
    }
}
