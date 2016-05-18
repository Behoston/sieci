package yeti.komunikaty;

public class PositionQuestion implements Komunikat {
    private final static byte type = 21;

    public PositionQuestion(Short id) {
        this.id = id;
    }

    private final Short id;

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
        return new byte[0];
    }
}
