package yeti.server;

import yeti.NotSupportedException;
import yeti.algo.AlgorithmContext;
import yeti.algo.results.ResultData;
import yeti.messages.*;
import yeti.utils.YetiInputStreamReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread {
    private Socket socket;
    private Yeti yeti;
    private YetiInputStreamReader yetiInputStreamReader;
    private AlgorithmContext algorithmContext;
    private DataInputStream dataInputStream;
    private ClientOutput clientOutput;

    ClientConnection(Socket socket, Yeti yeti) {
        this.socket = socket;
        this.yeti = yeti;
        this.algorithmContext = new AlgorithmContext();
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.yetiInputStreamReader = new YetiInputStreamReader(this, algorithmContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.clientOutput = new ClientOutput(socket);
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
                communicate = yetiInputStreamReader.read();
            } catch (IOException | NotSupportedException e) {
                e.printStackTrace();
                continue;
            }

            if (communicate.getType() == 1) {
                // Calculate
                Calculate calculate = (Calculate) communicate;
                algorithmContext.addAlgorithm(calculate.getId(), calculate.getType());
                yeti.calculate(calculate.getAlgorithm());
            } else if (communicate.getType() == 11) {
                // Cancel
                Cancel cancel = (Cancel) communicate;
                yeti.cancel(cancel.getId(), getIp());
            } else if (communicate.getType() == 21) {
                // PositionQuestion
                PositionQuestion positionQuestion = (PositionQuestion) communicate;
                PositionAnswer positionAnswer = yeti.getPosition(positionQuestion.getId(), getIp());

            }
        }
    }


    public ClientOutput getClientOutput() {
        return clientOutput;
    }
}
