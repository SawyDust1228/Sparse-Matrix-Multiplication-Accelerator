package pe.elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author 尹硕
 */
public abstract class Buffer {
    private int id;
    private int size;
    private ArrayList<LabelTuple> buffer;
    private Queue<LabelTuple> fifo;

    public ArrayList<LabelTuple> getBuffer() {
        return buffer;
    }

    public Queue<LabelTuple> getFifo() {
        return fifo;
    }

    public Buffer(int id, int size) {
        this.id = id;
        this.size = 2 * size;
        buffer = new ArrayList<>();
        fifo = new LinkedList<>();
    }

    public boolean isFull() {
        return buffer.size() == this.size;
    }

    public boolean containThisLabel(LabelTuple labelTuple) {
        return buffer.contains(labelTuple);
    }

    public LabelTuple drainData() {
        return fifo.poll();
    }
}
