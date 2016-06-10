package yeti.utils;

import yeti.ConnectionLostException;
import yeti.NotSupportedException;
import yeti.algo.Algorithm;
import yeti.algo.AlgorithmResolver;
import yeti.messages.Calculate;
import yeti.messages.Cancel;
import yeti.messages.Communicate;
import yeti.messages.PositionQuestion;
import yeti.server.ClientConnection;
import yeti.server.ClientOutput;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class YetiServerInputStreamReader {

    private DataInputStream inputStream;
    private String ip;
    private Lock lock;
    private ClientOutput clientOutput;

    public YetiServerInputStreamReader(ClientConnection clientConnection) {
        this.ip = clientConnection.getIp();
        this.inputStream = clientConnection.getDataInputStream();
        this.clientOutput = clientConnection.getClientOutput();
        this.lock = new ReentrantLock();
    }

    public Communicate read() throws IOException, NotSupportedException, ConnectionLostException {
        Byte type;
        lock.lock();
        try {
            type = inputStream.readByte();
        } catch (EOFException e) {
            throw new ConnectionLostException();
        }
        Communicate communicate;
        if (type == 1) {
            // Calculate
            communicate = readCalculate();
        } else if (type == 11) {
            // Cancel
            communicate = readCancel();
        } else if (type == 21) {
            // PositionQuestion
            communicate = readPositionQuestion();
        } else {
            // NotSupported
            throw new NotSupportedException();
        }
        lock.unlock();
        return communicate;
    }

    private Calculate readCalculate() throws IOException, NotSupportedException {
        Short id = inputStream.readShort();
        Integer packageId = inputStream.readInt();
        Short algorithmType = inputStream.readShort();
        Long length = inputStream.readLong();
        Algorithm algorithm = AlgorithmResolver.resolve(id, packageId, algorithmType, length, inputStream, ip, clientOutput);
        return new Calculate(algorithm);
    }

    private Cancel readCancel() throws IOException {
        Short id = inputStream.readShort();
        return new Cancel(id);
    }

    private PositionQuestion readPositionQuestion() throws IOException {
        Short id = inputStream.readShort();
        return new PositionQuestion(id);
    }

}
