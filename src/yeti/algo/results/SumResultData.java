package yeti.algo.results;

import java.io.DataOutputStream;
import java.io.IOException;

public class SumResultData implements ResultData {
    private Integer result;

    public SumResultData(Integer result) {
        this.result = result;
    }

    @Override
    public void writeResultToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(Byte.BYTES);
        dataOutputStream.writeInt(result);

    }

    public void add(Integer i) {
        result += i;
    }
}
