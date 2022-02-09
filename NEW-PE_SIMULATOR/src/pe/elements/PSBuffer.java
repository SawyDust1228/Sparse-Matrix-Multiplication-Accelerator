package pe.elements;

import utils.MatrixReader;

import java.util.Queue;

/**
 * @author 尹硕
 */
public class PSBuffer extends Buffer {
    private int id;
    private int size;
    private Queue<LabelTuple> sameCycleMergerFifo;
    private int count = 0;

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
        if (labelTuple != null) {
            if (!containThisLabel(labelTuple)) {
                labelTuple.renewTime();
                super.getBuffer().add(labelTuple);
            } else {
                count++;
            }

        }
    }

    public boolean isFifoEmpty() {
        return super.getFifo().isEmpty();
    }

    public boolean isBufferEmpty() {
        return super.getBuffer().isEmpty();
    }

    public void addTime() {
        for (LabelTuple labelTuple : super.getBuffer()) {
            labelTuple.addTime();
        }
    }

    public LabelTuple drainBufferData() throws Exception {
        if (super.isFull()) {
            LabelTuple labelTuple = null;
            int max = Integer.MIN_VALUE;
            for (LabelTuple item : super.getBuffer()) {
                if (item.getTime() > max) {
                    labelTuple = item;
                }
            }
            if (labelTuple == null) {
                throw new Exception("PS Buffer Exception");
            }
            super.getBuffer().remove(labelTuple);
            return labelTuple;
        } else {
            if (super.getFifo().isEmpty() && sameCycleMergerFifo.isEmpty()) {
                if (!super.getBuffer().isEmpty()) {
                    LabelTuple labelTuple = null;
                    int max = Integer.MIN_VALUE;
                    for (LabelTuple item : super.getBuffer()) {
                        if (item.getTime() > max) {
                            labelTuple = item;
                        }
                    }
                    if (labelTuple == null) {
                        throw new Exception("PS Buffer Exception");
                    }
                    super.getBuffer().remove(labelTuple);
                    return labelTuple;
                }
            }
        }
        return null;
    }

    public void setSameCycleMergerFifo(Queue<LabelTuple> fifo) {
        this.sameCycleMergerFifo = fifo;
    }

    public int getCount() {
        return count;
    }
}
