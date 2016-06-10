package yeti.server;

import yeti.ConnectionLostException;
import yeti.NotSupportedException;
import yeti.OverloadException;
import yeti.messages.*;
import yeti.utils.YetiServerInputStreamReader;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread {
    private Socket socket;
    private Yeti yeti;
    private YetiServerInputStreamReader yetiServerInputStreamReader;
    private DataInputStream dataInputStream;
    private ClientOutput clientOutput;

    ClientConnection(Socket socket, Yeti yeti) {
        this.socket = socket;
        this.yeti = yeti;
        this.clientOutput = new ClientOutput(this.socket);
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.yetiServerInputStreamReader = new YetiServerInputStreamReader(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public String getIp() {
        return socket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        while (true) {
            Communicate communicate;
            try {
                communicate = yetiServerInputStreamReader.read();
            } catch (IOException | NotSupportedException e) {
                e.printStackTrace();
                continue;
            } catch (ConnectionLostException e) {
                close();
                return;
            }

            if (communicate.getType() == 1) {
                // Calculate
                Calculate calculate = (Calculate) communicate;
                try {
                    yeti.calculate(calculate.getAlgorithm());
                } catch (OverloadException e) {
                    clientOutput.sendOverload(new Overload(calculate.getId(), calculate.getPackageId()));
                }
            } else if (communicate.getType() == 11) {
                // Cancel
                Cancel cancel = (Cancel) communicate;
                Cancelled cancelled = yeti.cancel(cancel.getId(), getIp());
                if (cancelled != null) {
                    // null znaczy, że jakieś obliczenie jest aktualnie liczone i ono samo wyśle wiadomość
                    clientOutput.sendCancelled(cancelled);
                }
            } else if (communicate.getType() == 21) {
                // PositionQuestion
                PositionQuestion positionQuestion = (PositionQuestion) communicate;
                PositionAnswer positionAnswer = yeti.getPosition(positionQuestion.getId(), getIp());
                clientOutput.sendPosition(positionAnswer);

            }
        }
    }

    public ClientOutput getClientOutput() {
        return clientOutput;
    }

    public void close() {
        System.out.println("Connection closed: " + getIp());
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
