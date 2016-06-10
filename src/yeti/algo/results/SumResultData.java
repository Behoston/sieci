package yeti.algo.results;

import java.io.DataOutputStream;
import java.io.IOException;

public class SumResultData implements ResultData {
    private Long result;

    public SumResultData(Long result) {
        this.result = result;
    }

    public Long getResult() {
        return result;
    }

    @Override
    public void writeResultToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(Byte.BYTES);
        dataOutputStream.writeLong(result);

    }

    public boolean add(int i) {
        result += i;
        if (result >= Long.MAX_VALUE) {
            System.out.println("Maximum value!!!");
            return false;
        } else if (result <= Long.MIN_VALUE) {
            System.out.println("Minimum value!!!");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SumResultData{" +
                "result=" + result +
                '}';
    }
}
