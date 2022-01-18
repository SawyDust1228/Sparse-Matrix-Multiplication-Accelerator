package pe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author 尹硕
 */

public class PeColumn {
    private int id;
    private int size;
    private ArrayList<PeUnit> column;
    private SameCycleMerger sameCyeleMerger;
    private HashMap<Integer, HashSet<Integer>> labelMap;

    public PeColumn(int id, int size) {
        column = new ArrayList<>();
        labelMap = new HashMap<>();
        this.id = id;
        this.size = size;
        for (int i = 0; i < size; i++) {
            column.add(new PeUnit());
        }
        sameCyeleMerger = new SameCycleMerger(size); // change the fifo(in the scmerger) size
    }

    public HashMap<Integer, HashSet<Integer>> getLabelMap() {
        return labelMap;
    }

    public ArrayList<PeUnit> getColumn() {
        return column;
    }

    public void setDrainId(int[] ids) {
        assert ids.length == size;
        for (int i = 0; i < size; i++) {
            column.get(i).setDrainId(ids[i]);
        }
    }


    public void setLabels(ArrayList<Integer> labels) {
        assert labels.size() == size;
        for (int i = 0; i < size; i++) {
            column.get(i).setLabel(labels.get(i));
        }
    }

    public boolean compute(ArrayList<Integer> streamingData) {
        assert streamingData.size() == size;
        for (int i = 0; i < size; i++) {
            column.get(i).compute(streamingData.get(i));
        }
        sameCyeleMerger.compute(getPeUnitResult());
        return sameCyeleMerger.getNeedStall();
    }

    private ArrayList<LabelTuple> getPeUnitResult() {
        ArrayList<LabelTuple> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LabelTuple value = column.get(i).getLabelTuples().poll();
            if (value == null) {
                result.add(new LabelTuple(-1, -1));
            } else if (value.isValid()) {
                result.add(value);
            } else {
                result.add(new LabelTuple(-1, -1));
            }
        }
        return result;
    }

    public LabelTuple drainData() {
        return sameCyeleMerger.drainData();
    }

    public void findConnection() {
        for (int i = 0; i < size; i++) {
            PeUnit unit = column.get(i);
            int label = unit.getLabel();
            if (labelMap.containsKey(label)) {
                labelMap.get(label).add(i);
            } else {
                HashSet<Integer> set = new HashSet<>();
                set.add(i);
                labelMap.put(label, set);
            }
        }
    }

    public boolean isEmpty() {
        return sameCyeleMerger.isEmpty();
    }

    public SameCycleMerger getSameCyeleMerger() {
        return sameCyeleMerger;
    }

}