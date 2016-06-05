package yeti.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class PositionAnswer implements Communicate {
    private static final byte TYPE = 61;
    private final Short id;
    private final Integer position;

    public PositionAnswer(Short id, Integer position) {
        this.id = id;
        this.position = position;
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
        dataOutputStream.writeInt(position);
    }

}
