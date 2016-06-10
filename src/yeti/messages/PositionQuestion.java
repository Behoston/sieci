package yeti.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class PositionQuestion implements Communicate {
    private final static byte TYPE = 21;
    private final Short id;

    public PositionQuestion(Short id) {
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

    @Override
    public String toString() {
        return "PositionQuestion{" +
                "id=" + id +
                '}';
    }
}
