package noc;


import pe.elements.LabelTuple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author xindust629
 */
public class Router {
    private int wires;
    private Queue<LabelTuple> fifos;


    public Router(int wires) {
        this.wires = wires;
        fifos = new LinkedList<>();
    }

    public void setWires(int wires) {
        this.wires = wires;
    }

    public void addFifo(ArrayList<LabelTuple> labelTuples) {
        fifos.addAll(labelTuples);
    }


    public ArrayList<LabelTuple> horizontalMovement() {
        ArrayList<LabelTuple> result = new ArrayList<>();
        for (int i = 0; i < wires; i++) {
            if (!fifos.isEmpty()) {
                result.add(fifos.poll());
            }
        }

        return result;
    }

    public boolean isEmpty() {
        return fifos.isEmpty();
    }


}
