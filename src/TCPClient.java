import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class TCPClient {
    public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String hania = "10.1.8.169";
        String asia = "10.20.6.254";
        String janek = "10.1.1.188";
        String ja = "10.1.8.62";
        Socket clientSocket = new Socket(ja, 6789);
        while (true) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sentence = inFromUser.readLine();
            outToServer.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);
            if (modifiedSentence.equals("KONIEC")) {
                break;
            }
        }
        clientSocket.close();
    }
}