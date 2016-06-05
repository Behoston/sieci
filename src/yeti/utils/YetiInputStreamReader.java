package yeti.utils;

import yeti.NotSupportedException;
import yeti.algo.Algorithm;
import yeti.algo.AlgorithmContext;
import yeti.algo.AlgorithmResolver;
import yeti.messages.*;
import yeti.messages.Error;
import yeti.server.ClientConnection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class YetiInputStreamReader {

    private DataInputStream inputStream;
    private ClientConnection clientConnection;
    private AlgorithmContext context;
    private Lock lock;

    public YetiInputStreamReader(InputStream inputStream, ClientConnection clientConnection, AlgorithmContext algorithmContext) {
        this.inputStream = new DataInputStream(inputStream);
        this.clientConnection = clientConnection;
        this.context = algorithmContext;
        this.lock = new ReentrantLock();
    }

    public Communicate
    read() throws IOException, NotSupportedException {
        lock.lock();
        Byte type = inputStream.readByte();
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
        } else if (type == 51) {
            // Result
            communicate = readResult();
        } else if (type == 61) {
            // PositionAnswer
            communicate = readPositionAnswer();
        } else if (type == 101) {
            // Cancelled
            communicate = readCancelled();
        } else if (type == 111) {
            // Overload
            communicate = readOverload();
        } else if (type == 121) {
            // Error
            communicate = readError();
        } else {
            // NotSupported
            throw new NotSupportedException();
        }
        lock.unlock();
        return communicate;
    }

    private Calculate readCalculate() throws IOException, NotSupportedException {
        Short id = inputStream.readShort();
        Short algorithmType = inputStream.readShort();
        Long length = inputStream.readLong();
        Algorithm algorithm = AlgorithmResolver.resolve(id, algorithmType, length, inputStream, clientConnection);
        return new Calculate(id, algorithm);
    }

    private Cancel readCancel() throws IOException {
        Short id = inputStream.readShort();
        return new Cancel(id);
    }

    private PositionQuestion readPositionQuestion() throws IOException {
        Short id = inputStream.readShort();
        return new PositionQuestion(id);
    }

    private Result readResult() throws IOException, NotSupportedException {
        Short id = inputStream.readShort();
        Byte algorithmId = context.identify(id);
        Long length = inputStream.readLong();
        Object data = AlgorithmResolver.resolveResult(algorithmId, length, inputStream);
        return new Result(id, data);
    }

    private PositionAnswer readPositionAnswer() throws IOException {
        Short id = inputStream.readShort();
        Integer position = inputStream.readInt();
        return new PositionAnswer(id, position);
    }

    private Cancelled readCancelled() throws IOException {
        Short id = inputStream.readShort();
        return new Cancelled(id);
    }

    private Overload readOverload() throws IOException {
        Short id = inputStream.readShort();
        return new Overload(id);
    }

    private Error readError() throws IOException {
        Short id = inputStream.readShort();
        return new Error(id);
    }
}