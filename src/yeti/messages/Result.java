package yeti.messages;

public class Result implements Communicate {

    private static final byte TYPE = 51;
    private final Short id;
    private final Object data;

    public Result(Short id, Object data) {
        this.id = id;
        this.data = data;
    }


    @Override
    public short getId() {
        return id;
    }

    @Override
    public byte getType() {
        return TYPE;
    }

}
