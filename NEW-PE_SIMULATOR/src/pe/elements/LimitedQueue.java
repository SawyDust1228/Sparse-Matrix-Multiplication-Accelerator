package pe.elements;

import java.util.ArrayList;
import java.util.LinkedList;

public class LimitedQueue<E> extends LinkedList<E> {
    private static final long serialVersionUID = 1L;
    private final int limit;
    private E pop;

    public LimitedQueue(int limit, E o) {
        this.limit = limit;
        for (int i = 0; i < limit; i++) {
            super.add(o);
        }
    }

    @Override
    public boolean add(E o) {
        super.addFirst(o);
        while (size() > limit) {
            pop = super.removeLast();
        }
        return true;
    }

    public E getPop() {
        return pop;
    }

    public void insert(int index, E o) {
        ArrayList<E> temp = new ArrayList<>(this);
        temp.add(index, o);
        for (int i = limit - 1; i >= 0; i--) {
            this.add(temp.get(i));
        }
    }


}

