package yeti.algo;

import javafx.util.Pair;
import yeti.InvalidDataException;
import yeti.algo.results.KnapsackResultData;
import yeti.messages.Cancelled;
import yeti.messages.Error;
import yeti.messages.Result;
import yeti.server.ClientOutput;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import static yeti.algo.State.*;

public class Knapsack implements Algorithm {
    private final static short ALGORITHM_NUMBER = 2;
    private Short id;
    private Integer packageId;
    private String ip;
    private List<Pair<Integer, Integer>> objects;
    private Integer capacity;
    private ClientOutput clientOutput;
    private Boolean interrupted;
    private KnapsackResultData result;
    private State state;

    public Knapsack(Short id, Integer packageId, String ip, List<Pair<Integer, Integer>> objects, Integer capacity,
                    ClientOutput clientOutput) {
        this.id = id;
        this.packageId = packageId;
        this.ip = ip;
        this.objects = objects;
        this.capacity = capacity;
        this.clientOutput = clientOutput;
        this.interrupted = false;
        this.state = WAITING;
    }


    @Override
    public void run() throws InvalidDataException {
        // TODO: 09.06.16 implement
        try {
            sendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public int getPackageId() {
        return packageId;
    }

    @Override
    public String getIp() {
        return ip;
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
    public void sendMessage() throws IOException {
        if (state == DONE) {
            clientOutput.sendResult(new Result(id, packageId, result));
        } else if (state == CANCELLED) {
            clientOutput.sendCancelled(new Cancelled(id));
        } else {
            clientOutput.sendError(new Error(id, packageId));
        }
    }


    private long getDataLength() {
        return (objects.size() * 2 + 2) * Integer.BYTES;
    }

    @Override
    public short getAlgorithmId() {
        return ALGORITHM_NUMBER;
    }

    @Override
    public void writeRequestToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(ALGORITHM_NUMBER);
        dataOutputStream.writeLong(getDataLength());
        for (Pair<Integer, Integer> p : objects) {
            dataOutputStream.writeInt(p.getValue());
            dataOutputStream.writeInt(p.getKey());
        }
        dataOutputStream.writeInt(capacity);
    }


}
