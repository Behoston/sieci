package yeti.algo.results;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ResultData {
    void writeResultToDataOutputStream(DataOutputStream dataOutputStream) throws IOException;
}
