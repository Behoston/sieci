package yeti.messages;

import yeti.algo.results.ResultData;

import java.io.DataOutputStream;
import java.io.IOException;

public class Result implements Communicate {

    private static final byte TYPE = 51;
    private final Short id;
    private final Integer packageId;
    private final ResultData resultData;

    public Result(Short id, Integer packageId, ResultData resultData) {
        this.id = id;
        this.packageId = packageId;
        this.resultData = resultData;
    }

    public ResultData getResultData() {
        return resultData;
    }

    @Override
    public short getId() {
        return id;
    }

    public int getPackageId() {
        return packageId;
    }

    @Override
    public byte getType() {
        return TYPE;
    }

    @Override
    public void writeToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(getType());
        dataOutputStream.writeShort(getId());
        dataOutputStream.writeInt(getPackageId());
        resultData.writeResultToDataOutputStream(dataOutputStream);
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", packageId=" + packageId +
                ", resultData=" + resultData +
                '}';
    }
}
