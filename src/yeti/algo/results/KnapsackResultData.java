package yeti.algo.results;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class KnapsackResultData implements ResultData {
    private List<Byte> result;

    public KnapsackResultData(List<Byte> data) {
        result = data;
    }

    @Override
    public void writeResultToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(result.size() * Byte.BYTES);
        for (Byte b : result) {
            dataOutputStream.writeByte(b);
        }
    }
}
