package yeti.komunikaty;

public class PositionAnswer implements Komunikat {
    @Override
    public short getId() {
        return 0;
    }

    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
