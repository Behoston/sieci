import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientUDP {
    public static void main(String[] args) {
        byte sendbuff[];
        byte recbuff[] = new byte[1024];
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            DatagramSocket gniazdo = new DatagramSocket();
            InetAddress adressip = InetAddress.getByName("localhost");
            while (true) {
                String napis = buf.readLine();
                sendbuff = napis.getBytes();
                DatagramPacket sendpack = new DatagramPacket(sendbuff, sendbuff.length, adressip, 9876);
                gniazdo.send(sendpack);
                DatagramPacket recpacket = new DatagramPacket(recbuff, recbuff.length);
                gniazdo.receive(recpacket);
                String duzynapis = new String(recpacket.getData());
                System.out.println("od serwera " + duzynapis);
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
