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

    @Override
    public ResultData merge(ResultData resultData) {
        KnapsackResultData other = (KnapsackResultData) resultData;
        return new KnapsackResultData(this.result + other.result);
    }

    @Override
    public String resultToString() {
        return result.toString();
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
