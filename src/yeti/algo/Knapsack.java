package yeti.algo;

import javafx.util.Pair;
import yeti.algo.results.KnapsackResultData;
import yeti.server.ClientConnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import static yeti.algo.State.*;

public class Knapsack implements Algorithm {
    private final static short TYPE = 2;
    private Short id;
    private List<Pair<Integer, Integer>> data;
    private Integer start;
    private Integer end;
    private ClientConnection clientConnection;
    private Boolean interrupted;
    private KnapsackResultData result;
    private State state;

    public Knapsack(Short id, List<Pair<Integer, Integer>> data, Integer start, Integer end, ClientConnection clientConnection) {
        this.id = id;
        this.data = data;
        this.start = start;
        this.end = end;
        this.clientConnection = clientConnection;
        this.interrupted = false;
        this.state = WAITING;
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
        return (data.size() * 2 + 2) * Integer.BYTES;
    }

    @Override
    public short getType() {
        return TYPE;
    }

    @Override
    public void writeRequestToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(TYPE);
        dataOutputStream.writeLong(getDataLength());
        for (Pair<Integer, Integer> p : data) {
            dataOutputStream.writeInt(p.getValue());
            dataOutputStream.writeInt(p.getKey());
        }
        dataOutputStream.writeInt(start);
        dataOutputStream.writeInt(end);
    }


}
