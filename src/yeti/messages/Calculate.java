package yeti.messages;


import yeti.algo.Algorithm;

import java.io.DataOutputStream;
import java.io.IOException;

public class Calculate implements Communicate {
    private final static Byte TYPE = 1;
    private Algorithm algorithm;

    public Calculate(Algorithm algorithm) {

        this.algorithm = algorithm;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }


    @Override
    public byte getType() {
        return TYPE;
    }

    @Override
    public void writeToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(TYPE);
        dataOutputStream.writeShort(algorithm.getId());
        algorithm.writeRequestToDataOutputStream(dataOutputStream);
    }

    @Override
    public short getId() {
        return algorithm.getId();
    }


}
