package yeti;

import java.util.BitSet;

public class test {
    public static void main(String[] argv) {
        BitSet bitSet = new BitSet(15);
        bitSet.set(0, true);
        BitSet one = new BitSet(1);
        one.set(0, true);

        bitSet.and(one);
        System.out.println(bitSet);
    }
}
