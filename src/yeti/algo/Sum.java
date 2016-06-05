package yeti.algo;

import yeti.InvalidDataException;
import yeti.algo.results.SumResultData;
import yeti.server.ClientConnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static yeti.algo.State.*;

public class Sum implements Algorithm {

    private final static short TYPE = 1;

    private Short id;
    private List<Integer> data;
    private SumResultData result;
    private ClientConnection clientConnection;
    private Boolean interrupted;
    private Lock lock = new ReentrantLock();
    private State state;

    public Sum(Short id, List<Integer> data, ClientConnection clientConnection) {
        this.id = id;
        this.data = data;
        this.clientConnection = clientConnection;
        interrupted = false;
        state = WAITING;
    }

    @Override
    public void run() throws InvalidDataException {
        lock.lock();
        state = CALCULATING;
        result = new SumResultData(0);
        for (Integer i : data) {
            result.add(i);
            if (interruptionCheck()) {
                state = CANCELLED;
                sendMessage();
                return;
            }
        }
        state = DONE;
        sendMessage();
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public String getIp() {
        return clientConnection.getIp();
    }

    @Override
    public void interrupt() {
        interrupted = true;
    }

    @Override
    public boolean interruptionCheck() {
        return interrupted;
    }

    @Override
    public void sendMessage() {
        if (state == DONE) {
            clientConnection.sendResult(id, result);
        } else if (state == CANCELLED) {
            clientConnection.sendCancelled();
        } else {
            clientConnection.sendError();
        }
    }


    private long getDataLength() {
        return data.size() * Integer.BYTES;
    }

    @Override
    public short getType() {
        return TYPE;
    }

    @Override
    public void writeRequestToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(TYPE);
        dataOutputStream.writeLong(getDataLength());
        for (Integer i : data) {
            dataOutputStream.writeInt(i);
        }
    }


}
