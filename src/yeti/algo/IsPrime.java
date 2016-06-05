package yeti.algo;

import yeti.InvalidDataException;
import yeti.server.ClientConnection;

import java.io.DataOutputStream;
import java.io.IOException;

public class IsPrime implements Algorithm {
    private final static short TYPE = 3;
    private Short id;
    private Integer start;
    private Integer end;
    private Integer number;
    private ClientConnection clientConnection;
    private Boolean interrupted;
    private Integer result;

    public IsPrime(Short id, Integer start, Integer end, Integer number, ClientConnection clientConnection) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.number = number;
        this.clientConnection = clientConnection;
        this.interrupted = false;
    }


    @Override
    public void run() throws InvalidDataException {
        result = 0;
        for (int i = start; i == end; i++) {

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
        this.interrupted = true;

    }

    @Override
    public boolean interruptionCheck() {
        return interrupted;
    }


    private long getDataLength() {
        return 3 * Integer.BYTES;
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
        dataOutputStream.writeInt(start);
        dataOutputStream.writeInt(end);
        dataOutputStream.writeInt(number);
    }

    @Override
    public void writeResultToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(getResultLength());
        dataOutputStream.writeInt(result);
    }
}
