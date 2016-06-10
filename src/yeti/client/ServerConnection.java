package yeti.client;

import yeti.NotSupportedException;
import yeti.algo.AlgorithmContext;
import yeti.messages.Communicate;
import yeti.utils.YetiClientInputStreamReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Thread {

    private Socket socket;
    private AlgorithmContext context;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ServerConnection(String ip, AlgorithmContext context) {
        this.context = context;
        try {
            socket = new Socket(ip, 6454);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public String getServerIp(){
        return socket.getInetAddress().getHostAddress();
    }


    public void sendCommunicate(Communicate communicate){
        try {
            communicate.writeToDataOutputStream(dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        YetiClientInputStreamReader streamReader = new YetiClientInputStreamReader(dataInputStream, context);
        Communicate communicate;
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                communicate = streamReader.read();
                System.out.println(communicate);
            } catch (IOException | NotSupportedException e) {
                e.printStackTrace();
            }
            // TODO: 10.06.16 tutaj trzeba rozpoznawać komunikaty i przekazywać je do sklejania
        }
    }

}
