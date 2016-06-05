package yeti.server;

import yeti.NotSupportedException;
import yeti.algo.AlgorithmContext;
import yeti.messages.*;
import yeti.utils.YetiInputStreamReader;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread {
    private Socket socket;
    private Yeti yeti;
    private YetiInputStreamReader yetiInputStreamReader;
    private AlgorithmContext algorithmContext;

    ClientConnection(Socket socket, Yeti yeti) {
        this.socket = socket;
        this.yeti = yeti;
        this.algorithmContext = new AlgorithmContext();
        try {
            this.yetiInputStreamReader = new YetiInputStreamReader(socket.getInputStream(), this, algorithmContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                yeti.calculate(calculate.getAlgorithm());
            } else if (communicate.getType() == 11) {
                // Cancel
                Cancel cancel = (Cancel) communicate;
                yeti.cancel(cancel.getId(), getIp());
            } else if (communicate.getType() == 21) {
                // PositionQuestion
                PositionQuestion positionQuestion = (PositionQuestion) communicate;
                Integer number = yeti.getPosition(positionQuestion.getId(), getIp());
                PositionAnswer answer = new PositionAnswer(positionQuestion.getId(), number);
            }
        }

    }
}
