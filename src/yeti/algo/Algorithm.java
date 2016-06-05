package yeti.algo;

import yeti.InvalidDataException;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Algorithm {

    void run() throws InvalidDataException;

    short getId();

    String getIp();

    void interrupt();

    boolean interruptionCheck();

    void sendMessage();

    short getType();

    void writeRequestToDataOutputStream(DataOutputStream dataOutputStream) throws IOException;

}
