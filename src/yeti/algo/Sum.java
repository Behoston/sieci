package yeti.algo;

import yeti.InvalidDataException;
import yeti.algo.results.SumResultData;
import yeti.messages.Cancelled;
import yeti.messages.Error;
import yeti.messages.Result;
import yeti.server.ClientOutput;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static yeti.algo.State.*;

public class Sum implements Algorithm {

    private final static short ALGORITHM_NUMBER = 1;

    private Short id;
    private List<Integer> data;
    private SumResultData result;
    private String ip;
    private Boolean interrupted;
    private Lock lock = new ReentrantLock();
    private State state;
    private Integer packageId;
    private ClientOutput clientOutput;

    public Sum(Short id, Integer packageId, List<Integer> data, String ip, ClientOutput clientOutput) {
        this.id = id;
        this.packageId = packageId;
        this.ip = ip;
        this.clientOutput = clientOutput;
        this.data = data;
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
                try {
                    sendMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        state = DONE;
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
        return data.size() * Integer.BYTES;
    }

    @Override
    public short getAlgorithmId() {
        return ALGORITHM_NUMBER;
    }

    @Override
    public void writeRequestToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(ALGORITHM_NUMBER);
        dataOutputStream.writeLong(getDataLength());
        for (Integer i : data) {
            dataOutputStream.writeInt(i);
        }
    }


}
