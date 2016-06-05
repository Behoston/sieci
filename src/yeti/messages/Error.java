package yeti.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class Error implements Communicate {
    private static final byte TYPE = 121;
    private final Short id;

    public Error(Short id) {
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
        dataOutputStream.writeShort(id);
    }

}
