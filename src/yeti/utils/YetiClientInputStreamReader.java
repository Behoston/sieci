package yeti.utils;

import yeti.NotSupportedException;
import yeti.algo.AlgorithmContext;
import yeti.algo.AlgorithmResolver;
import yeti.algo.results.ResultData;
import yeti.messages.*;
import yeti.messages.Error;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class YetiClientInputStreamReader {
    private DataInputStream inputStream;
    private AlgorithmContext context;
    private Lock lock;

    public YetiClientInputStreamReader(DataInputStream inputStream, AlgorithmContext context) {
        this.inputStream = inputStream;
        this.context = context;
        this.lock = new ReentrantLock();

    }

    public Communicate read() throws IOException, NotSupportedException {
        lock.lock();
        Byte type = inputStream.readByte();
        Communicate communicate;
        if (type == 51) {
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


    private Result readResult() throws IOException, NotSupportedException {
        Short id = inputStream.readShort();
        Integer packageId = inputStream.readInt();
        Short algorithmId = context.identify(id);
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

    private yeti.messages.Error readError() throws IOException {
        Short id = inputStream.readShort();
        Integer packageId = inputStream.readInt();
        return new Error(id, packageId);
    }
}

