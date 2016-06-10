package yeti.algo;

import yeti.algo.results.IsPrimeResultData;
import yeti.messages.Cancelled;
import yeti.messages.Error;
import yeti.messages.Result;
import yeti.server.ClientOutput;

import java.io.DataOutputStream;
import java.io.IOException;

import static yeti.algo.State.*;

public class IsPrime implements Algorithm {
    private final static short ALGORITHM_TYPE = 3;
    private Short id;
    private Integer packageId;
    private Integer start;
    private Integer end;
    private Integer number;
    private String ip;
    private Boolean interrupted;
    private IsPrimeResultData result;
    private State state;
    private ClientOutput clientOutput;

    /**
     * @param ip           only required on server side
     * @param clientOutput only required on server side
     */
    public IsPrime(Short id, Integer packageId, Integer start, Integer end, Integer number, String ip, ClientOutput clientOutput) {
        this.id = id;
        this.packageId = packageId;
        this.ip = ip;
        this.clientOutput = clientOutput;
        this.start = start;
        if (this.start <= 2) {
            this.start = 3;
        }
        this.number = number;
        this.end = end;
        if (this.end >= number) {
            this.end = number - 1;
        }
        this.interrupted = false;
        this.state = WAITING;
    }


    @Override
    public void run() {
        state = CALCULATING;
        result = new IsPrimeResultData(0);
        for (int i = start; i != end; i++) {
            if (interrupted) {
                state = CANCELLED;
                try {
                    sendMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            if (number % i == 0) {
                result.setResult(i);
                break;
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
        this.interrupted = true;

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
        return 3 * Integer.BYTES;
    }


    @Override
    public short getAlgorithmId() {
        return ALGORITHM_TYPE;
    }

    @Override
    public void writeRequestToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(ALGORITHM_TYPE);
        dataOutputStream.writeLong(getDataLength());
        dataOutputStream.writeInt(start);
        dataOutputStream.writeInt(end);
        dataOutputStream.writeInt(number);
    }

    @Override
    public String toString() {
        return "IsPrime{" +
                "id=" + id +
                ", packageId=" + packageId +
                ", start=" + start +
                ", end=" + end +
                ", number=" + number +
                ", ip='" + ip + '\'' +
                ", interrupted=" + interrupted +
                ", result=" + result +
                ", state=" + state +
                '}';
    }
}
