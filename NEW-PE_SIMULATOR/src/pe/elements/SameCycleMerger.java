package pe.elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author 尹硕
 */

public class SameCycleMerger {
    private int size;
    private Queue<LabelTuple> fifo = new LinkedList<>();
    private boolean needStall = false;

    public SameCycleMerger(int size) {
        this.size = size;
    }

    private ArrayList<LabelTuple> merge(ArrayList<LabelTuple> labelTuples) {
        HashSet<LabelTuple> set = new HashSet<>();
        for (LabelTuple item : labelTuples) {
            if (item.isValid()) {
                set.add(item);
            }
        }
        ArrayList<LabelTuple> result = new ArrayList<>(set);
        return result;
    }

    private void isFull(int numOfMergedData) {
        needStall = fifo.size() + numOfMergedData > size;
    }

    public void compute(ArrayList<LabelTuple> labelTuples) {
        ArrayList<LabelTuple> comingData = merge(labelTuples);
        int numOfMergedData = comingData.size();
        isFull(numOfMergedData);
        if (needStall) {
            return;
        }
        fifo.addAll(comingData);
    }

    public LabelTuple drainData() {
        return fifo.poll();
    }

    public boolean isEmpty() {
        return fifo.isEmpty();
    }

    public Queue<LabelTuple> getFifo() {
        return fifo;
    }

    public boolean getNeedStall() {
        return needStall;
    }


}
