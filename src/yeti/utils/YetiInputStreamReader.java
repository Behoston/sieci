package yeti.utils;

import yeti.NotSupportedException;
import yeti.algo.Algorithm;
import yeti.algo.AlgorithmContext;
import yeti.algo.AlgorithmResolver;
import yeti.algo.results.ResultData;
import yeti.messages.*;
import yeti.messages.Error;
import yeti.server.ClientConnection;
import yeti.server.ClientOutput;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class YetiInputStreamReader {

    private DataInputStream inputStream;
    private String ip;
    private AlgorithmContext context;
    private Lock lock;
    private ClientOutput clientOutput;

    public YetiInputStreamReader(ClientConnection clientConnection, AlgorithmContext algorithmContext) {
        this.ip = clientConnection.getIp();
        this.inputStream = clientConnection.getDataInputStream();
        this.clientOutput = clientConnection.getClientOutput();
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

    private Result readResult() throws IOException, NotSupportedException {
        Short id = inputStream.readShort();
        Integer packageId = inputStream.readInt();
        Byte algorithmId = context.identify(id);
        Long length = inputStream.readLong();
        ResultData resultData = AlgorithmResolver.resolveResult(algorithmId, length, inputStream);
        return new Result(id, packageId, resultData);
    }

    private PositionAnswer readPositionAnswer() throws IOException {
        Short id = inputStream.readShort();
        Integer length = inputStream.readInt();
        Integer position = inputStream.readInt();
        return new PositionAnswer(id, length, position);
    }

    private Cancelled readCancelled() throws IOException {
        Short id = inputStream.readShort();
        return new Cancelled(id);
    }

    private Overload readOverload() throws IOException {
        Short id = inputStream.readShort();
        Integer packageId = inputStream.readInt();
        return new Overload(id, packageId);
    }

    private Error readError() throws IOException {
        Short id = inputStream.readShort();
        Integer packageId = inputStream.readInt();
        return new Error(id, packageId);
    }
}
