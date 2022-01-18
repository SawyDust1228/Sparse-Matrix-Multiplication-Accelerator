package pe;

import java.util.*;

/**
 * @author 尹硕
 */
public class PSBuffer extends Buffer {
    private int id;
    private int size;

    public PSBuffer(int id, int size) {
        super(id, size);
        this.id = id;
        this.size = size;
    }

    public void addLabelTuple(LabelTuple labelTuple) {
        super.getFifo().add(labelTuple);
    }

    public void compute() {
        LabelTuple labelTuple = super.getFifo().poll();
        if (labelTuple != null && !containThisLabel(labelTuple)) {
            super.getBuffer().add(labelTuple);
        }
    }

    public boolean isFifoEmpty() {
        return super.getFifo().isEmpty();
    }

    public boolean isBufferEmpty() {
        return super.getBuffer().isEmpty();
    }


}
