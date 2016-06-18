package yeti.algo;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Algorithm {

    void run();

    short getId();

    int getPackageId();

    String getIp();

    void interrupt();

    boolean interruptionCheck();

    void sendMessage() throws IOException;

    short getAlgorithmId();

    void writeRequestToDataOutputStream(DataOutputStream dataOutputStream) throws IOException;

    @Override
    String toString();

}
