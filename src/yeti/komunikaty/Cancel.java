package yeti.komunikaty;

import static yeti.utils.ByteHelper.*;

public class Cancel implements Komunikat {
    private final Byte type = 11;

    public Cancel(Short id) {
        this.id = id;
    }

    private Short id;

    @Override
    public short getId() {
        return id;
    }

    @Override
    public byte getType() {
        return type;
    }

    @Override
    public byte[] toByteArray() {
        return concat(byteToBytes(type), shortToBytes(id));
    }
}
