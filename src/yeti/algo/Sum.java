package yeti.algo;

import yeti.InvalidDataException;
import yeti.server.ClientConnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Sum implements Algorithm {

    private final static short TYPE = 1;

    private Short id;
    private List<Integer> data;
    private Integer result;
    private ClientConnection clientConnection;
    private Boolean interrupted;
    private Lock lock = new ReentrantLock();

    public Sum(Short id, List<Integer> data, ClientConnection clientConnection) {
        this.id = id;
        this.data = data;
        this.clientConnection = clientConnection;
        interrupted = false;

    }

    @Override
    public void run() throws InvalidDataException {
        lock.lock();
        result = 0;
        for (Integer i : data) {
            result += i;
            if (interruptionCheck()) {
                sendMessage();
                break;
            }
        }
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


    private long getDataLength() {
        return data.size() * Integer.BYTES;
    }

    private long getResultLength() {
        return Integer.BYTES;
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

    @Override
    public void writeResultToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(getResultLength());
        dataOutputStream.writeInt(result);
    }

}
