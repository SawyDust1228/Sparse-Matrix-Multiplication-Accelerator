package noc;

import pe.elements.LabelTuple;

import java.util.ArrayList;
import java.util.HashSet;

public class Sram {
    private HashSet<LabelTuple> set = new HashSet<>();
    private int count = 0;

    public Sram() {

    }

    public void addLabelTuples(ArrayList<LabelTuple> labelTuples) {
        for (LabelTuple item : labelTuples) {
            if (set.contains(item)) {
                count++;
            }
            set.add(item);
        }
    }

    public HashSet<LabelTuple> getSet() {
        return set;
    }

    public int getCount() {
        return count;
    }
}
