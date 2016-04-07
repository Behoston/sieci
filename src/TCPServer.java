import java.io.*;
import java.net.*;

class TCPServer {
    public static void main(String argv[]) throws Exception {
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);

        Socket connectionSocket = welcomeSocket.accept();
        while (true) {
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Remote IP: " + connectionSocket.getInetAddress().getHostAddress());
            System.out.println("Received: " + clientSentence);
            System.out.println("###########################");
            if (clientSentence != null) {
                capitalizedSentence = clientSentence.toUpperCase() + '\n';
            } else {
                capitalizedSentence = "NULL";
            }
            outToClient.writeBytes(capitalizedSentence);
            if (capitalizedSentence.equals("KONIEC\n")) {
                System.out.println("KONIEC");
                break;
            }
        }
        connectionSocket.close();
    }
}