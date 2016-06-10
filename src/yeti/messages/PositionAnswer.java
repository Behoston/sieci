package yeti.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class PositionAnswer implements Communicate {
    private static final byte TYPE = 61;
    private final Short id;
    private final Integer length;
    private final Integer position;

    public PositionAnswer(Short id, Integer length, Integer position) {
        this.id = id;
        this.position = position;
        this.length = length;
    }

    @Override
    public short getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    @Override
    public byte getType() {
        return TYPE;
    }

    @Override
    public void writeToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(TYPE);
        dataOutputStream.writeShort(id);
        dataOutputStream.writeInt(length);
        dataOutputStream.writeInt(position);
    }

    @Override
    public String toString() {
        return "PositionAnswer{" +
                "id=" + id +
                ", length=" + length +
                ", position=" + position +
                '}';
    }
}
