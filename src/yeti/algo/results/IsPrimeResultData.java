package yeti.algo.results;

import java.io.DataOutputStream;
import java.io.IOException;

public class IsPrimeResultData implements ResultData {
    private Integer result;

    public IsPrimeResultData(int result) {
        this.result = result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public void writeResultToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(Integer.BYTES);
        dataOutputStream.writeInt(result);
    }

    @Override
    public String toString() {
        return "IsPrimeResultData{" +
                "result=" + result +
                '}';
    }
}
