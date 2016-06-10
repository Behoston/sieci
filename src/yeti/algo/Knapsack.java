package yeti.algo;

import javafx.util.Pair;
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

    /**
     * @param ip           only required on server side
     * @param clientOutput only required on server side
     */
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
    public void run() {
        result = new KnapsackResultData(0L);
        state = CALCULATING;
        Long combination_number = 0L;
        int dataLength = objects.size();
        Long last_combination_number = (long) Math.pow(2, dataLength) - 1;
        Boolean[] combination;
        for (; combination_number <= last_combination_number; combination_number++) {
            if (interruptionCheck()) {
                state = CANCELLED;
                try {
                    sendMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            combination = convertToBinary(combination_number);
            long actualCapacity = 0;
            long actualValue = 0;
            for (int i = 0; i != dataLength; i++) {
                if (combination[i]) {
                    Pair<Integer, Integer> item = objects.get(i);
                    Integer capacity = item.getKey();
                    Integer value = item.getValue();
                    if (actualCapacity + capacity > this.capacity) {
                        // kiedy plecak będzie zbyt przepełniony to nie ma sensu liczyć dalej
                        break;
                    } else {
                        // jeśli się zmieści
                        actualValue += value;
                        actualCapacity += capacity;
                        if (actualValue > result.get()) {
                            // jak jest większa wartość to zapisać
                            result.set(actualValue);
                        }
                    }
                }
            }
        }
        state = DONE;
        try {
            sendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean[] convertToBinary(long i) {
        Boolean[] result = new Boolean[objects.size()];
        int position = 0;
        while (i != 0) {
            result[position] = (i % 2) == 1;
            i /= 2;
            position++;
        }
        for (; position != result.length; position++) {
            result[position] = false;
        }
        return result;
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
        return (objects.size() * 2 + 1) * Integer.BYTES;
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
            dataOutputStream.writeInt(p.getKey());
            dataOutputStream.writeInt(p.getValue());
        }
        dataOutputStream.writeInt(capacity);
    }

    @Override
    public String toString() {
        return "Knapsack{" +
                "id=" + id +
                ", packageId=" + packageId +
                ", ip='" + ip + '\'' +
                ", capacity=" + capacity +
                ", interrupted=" + interrupted +
                ", result=" + result +
                ", state=" + state +
                '}';
    }
}
