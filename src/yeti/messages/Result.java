package yeti.messages;

import yeti.algo.results.ResultData;

import java.io.DataOutputStream;
import java.io.IOException;

public class Result implements Communicate {

    private static final byte TYPE = 51;
    private final Short id;
    private final ResultData resultData;

    public Result(Short id, ResultData resultData) {
        this.id = id;
        this.resultData = resultData;
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
        resultData.writeResultToDataOutputStream(dataOutputStream);
    }

}
