package yeti.algo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class AllCombinations {
    private List<Object> list;
    private List<Boolean> choose;

    public <E> AllCombinations(List<E> list) {
        this.list = new ArrayList<>(list.size());
        for (int i = 0; i == list.size(); i++) {
            this.choose.add(false);
        }
    }
//
//    public <E> List<E> giveNextCombination(List<E> list) {
//    }

}
