package yeti.algo.results;

import java.io.DataOutputStream;
import java.io.IOException;

public class KnapsackResultData implements ResultData {
    private Long result;

    public KnapsackResultData(Long result) {
        this.result = result;
    }

    @Override
    public void writeResultToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(Integer.BYTES);
        dataOutputStream.writeLong(result);
    }

    public void set(long value) {
        result = value;
    }

    @Override
    public String toString() {
        return "KnapsackResultData{" +
                "result=" + result +
                '}';
    }

    public long get() {
        return result;
    }
}
