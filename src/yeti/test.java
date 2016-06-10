package yeti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {


    public static Boolean[] convertToBinary(long i) {
        List<Integer> objects = new ArrayList<>();
        objects.add(5);
        objects.add(5);
        objects.add(5);
        Boolean[] result = new Boolean[objects.size()];
        int position = 0;
        while (i != 0) {
            result[position] = (i % 2) == 1;
            i /= 2;
            position++;
        }
        for (; position != result.length; position++) {
            result[position] = false;
        }
        return result;
    }

    public static void main(String[] argv) {
//        for (String s : argv) {
//            System.out.println(s);
//        }
//        BitSet bitSet = new BitSet(15);
//        bitSet.set(0, true);
//        BitSet one = new BitSet(1);
//        one.set(0, true);
//
//        bitSet.and(one);
//        System.out.println(bitSet);

        Integer i = 5;
        Integer j = 2;
        System.out.println(i / j);
        System.out.println(Arrays.toString(convertToBinary(5L)));
    }


}
