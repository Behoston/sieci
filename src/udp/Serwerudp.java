package udp;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Serwerudp {
    public static void main(String[] args) throws IOException {
        byte sendbuff[];
        byte recbuff[];
        try {
            DatagramSocket gniazdoudp = new DatagramSocket(9876);
            while (true) {
                recbuff = new byte[1024];
                DatagramPacket recpacket = new DatagramPacket(recbuff, recbuff.length);
                gniazdoudp.receive(recpacket);
                String napis = new String(recpacket.getData());
                System.out.println("Otrzymano " + napis);
                InetAddress adressip = recpacket.getAddress();
                int port = recpacket.getPort();
                String duzynapis = napis.toUpperCase();
                sendbuff = duzynapis.getBytes();
                DatagramPacket sendpacket = new DatagramPacket(sendbuff, sendbuff.length, adressip, port);
                gniazdoudp.send(sendpacket);
            }

        } catch (SocketException ex) {
            Logger.getLogger(Serwerudp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
