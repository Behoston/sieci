package yeti.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class Cancelled implements Communicate {
    private static final byte TYPE = 101;
    private final Short id;

    public Cancelled(Short id) {
        this.id = id;
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public byte getType() {
        return TYPE;
    }

    @Override
    public void writeToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(TYPE);

    }
}
