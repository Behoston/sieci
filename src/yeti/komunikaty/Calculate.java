package yeti.komunikaty;


import static yeti.utils.ByteHelper.*;

public class Calculate implements Komunikat {
    private final static Byte type = 1;
    private Short id;
    private Integer package_id;
    private Short algorithm;
    private Long length;
    private byte[] dataArray;

    public Calculate(Short id, Integer package_id, Short algorithm, Long length, byte[] dataArray) {
        this.id = id;
        this.package_id = package_id;
        this.algorithm = algorithm;
        this.length = length;
        this.dataArray = dataArray;
    }


    public byte getType() {
        return type;
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public byte[] toByteArray() {
        return concat(byteToBytes(type), shortToBytes(id), intToBytes(package_id), shortToBytes(algorithm), longToBytes(length), dataArray);
    }

}
