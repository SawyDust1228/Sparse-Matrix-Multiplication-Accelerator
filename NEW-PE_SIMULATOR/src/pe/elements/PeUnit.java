package pe.elements;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author 尹硕
 */

public class PeUnit {
    private int label = -1;
    private int drainId = -1;
    private Queue<LabelTuple> labelTuples = new LinkedList<>();

    public PeUnit() {

    }

    public int getDrainId() {
        return drainId;
    }

    public void setDrainId(int drainId) {
        this.drainId = drainId;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    public Queue<LabelTuple> getLabelTuples() {
        return labelTuples;
    }

    public LabelTuple compute(int streamingLabel) {
        LabelTuple result = new LabelTuple(label, streamingLabel);
        result.setDrainID(drainId);
        labelTuples.add(result);
        return result;
    }
}
