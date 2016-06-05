package yeti.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class Overload implements Communicate {
    private static final byte TYPE = 111;
    private final Short id;

    public Overload(Short id) {
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
