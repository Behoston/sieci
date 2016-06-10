package yeti.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class Overload implements Communicate {
    private static final byte TYPE = 111;
    private final Short id;
    private final Integer packageId;

    public Overload(Short id, Integer packageId) {
        this.id = id;
        this.packageId = packageId;
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public byte getType() {
        return TYPE;
    }

    public int getPackageId() {
        return packageId;
    }

    @Override
    public void writeToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(TYPE);
        dataOutputStream.writeShort(id);
        dataOutputStream.writeInt(packageId);
    }

    @Override
    public String toString() {
        return "Overload{" +
                "id=" + id +
                ", packageId=" + packageId +
                '}';
    }
}
