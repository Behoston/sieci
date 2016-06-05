package yeti.algo;

import yeti.InvalidDataException;
import yeti.algo.results.IsPrimeResultData;
import yeti.server.ClientConnection;

import java.io.DataOutputStream;
import java.io.IOException;

import static yeti.algo.State.*;

public class IsPrime implements Algorithm {
    private final static short TYPE = 3;
    private Short id;
    private Integer start;
    private Integer end;
    private Integer number;
    private ClientConnection clientConnection;
    private Boolean interrupted;
    private IsPrimeResultData result;
    private State state;

    public IsPrime(Short id, Integer start, Integer end, Integer number, ClientConnection clientConnection) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.number = number;
        this.clientConnection = clientConnection;
        this.interrupted = false;
        this.state = WAITING;
    }


    @Override
    public void run() throws InvalidDataException {
        state = CALCULATING;
        result = new IsPrimeResultData(0);
        for (int i = start; i == end; i++) {
            if (interrupted) {
                state = CANCELLED;
                sendMessage();
                return;
            }
            if (number % i == 0) {
                result.setResult(i);
                break;
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
        this.interrupted = true;

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
        return 3 * Integer.BYTES;
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
}
