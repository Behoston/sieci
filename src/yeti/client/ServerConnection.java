package yeti.client;

import yeti.NotSupportedException;
import yeti.algo.AlgorithmContext;
import yeti.messages.Communicate;
import yeti.messages.PositionAnswer;
import yeti.messages.Result;
import yeti.utils.YetiClientInputStreamReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ServerConnection extends Thread {

    private Socket socket;
    private AlgorithmContext context;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Integrator integrator;

    public ServerConnection(String ip, AlgorithmContext context, Integrator integrator) {
        this.context = context;
        this.integrator = integrator;
        try {
            socket = new Socket(ip, 6454);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public String getServerIp() {
        return socket.getInetAddress().getHostAddress();
    }


    public void sendCommunicate(Communicate communicate) {
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
        while (true) {
            try {
                communicate = streamReader.read();
                if (communicate.getType() == 51) {
                    // Result
                    integrator.addResultResponse((Result) communicate);
                } else if (communicate.getType() == 61) {
                    // PositionAnswer
                    integrator.addStatusResponse((PositionAnswer) communicate, getServerIp());
                } else {
                    // Other communicate
                    System.out.println(communicate);
                }
            } catch (SocketException closed) {
                break;
            } catch (IOException | NotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    public void quit() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
