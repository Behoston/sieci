package yeti.komunikaty;

import static yeti.utils.ByteHelper.*;

public class Result implements Komunikat {

    private static final Byte type = 51;
    private final Short id;
    private final Integer package_id;
    private final Long length;
    private final byte[] data;

    public Result(Short id, Integer package_id, Long length, byte[] data) {
        this.id = id;
        this.package_id = package_id;
        this.length = length;
        this.data = data;
    }


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
        return concat(byteToBytes(type), shortToBytes(id), intToBytes(package_id), longToBytes(length), data);
    }
}
