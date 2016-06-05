package yeti.messages;


import java.io.DataOutputStream;
import java.io.IOException;

public interface Communicate {

    short getId();

    byte getType();

    void writeToDataOutputStream(DataOutputStream dataOutputStream) throws IOException;

}
