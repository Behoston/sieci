package yeti.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class Cancel implements Communicate {
    private final static byte TYPE = 11;
    private final Short id;

    public Cancel(Short id) {
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
