package noc;

import pe.SubPeArray;
import pe.elements.LabelTuple;

import java.util.ArrayList;
import java.util.HashMap;

public class RouterMap {
    private int SIZE;
    private int PE_SIZE;
    private int wires;
    private HashMap<Integer, HashMap<Integer, SubPeArray>> subPeArrays;
    private HashMap<Integer, ArrayList<Router>> idRouterMap = new HashMap<>();
    private Sram sram;

    public RouterMap(int SIZE, int PE_SIZE, int wires, HashMap<Integer, HashMap<Integer, SubPeArray>> subPeArrays, Sram sram) {
        this.SIZE = SIZE;
        this.wires = wires;
        this.PE_SIZE = PE_SIZE;
        this.subPeArrays = subPeArrays;
        this.sram = sram;
        build();
    }

    public void horizontalMovement() throws Exception {
        // Sram movement
        for (Integer key : idRouterMap.keySet()) {
            ArrayList<Router> line = idRouterMap.get(key);
            Router lastOne = line.get(SIZE - 1);
            ArrayList<LabelTuple> data = lastOne.horizontalMovement();
            sram.addLabelTuples(data);
        }
        // Router movement
        for (Integer key : idRouterMap.keySet()) {
            ArrayList<Router> line = idRouterMap.get(key);
            for (int i = SIZE - 1; i > 0; i--) {
                ArrayList<LabelTuple> data = line.get(i - 1).horizontalMovement();
                line.get(i).addFifo(data);
            }
        }
        for (Integer row : subPeArrays.keySet()) {
            for (Integer column : subPeArrays.get(row).keySet()) {
                SubPeArray sub = subPeArrays.get(row).get(column);
                Router router = idRouterMap.get(row).get(column);
                ArrayList<LabelTuple> data = sub.drainData();
                router.addFifo(data);
            }
        }
    }

    private void build() {
        for (int i = 0; i < SIZE; i++) {
            ArrayList<Router> list = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                list.add(new Router(wires));
            }
            idRouterMap.put(i, list);
        }
    }

    public boolean isFinish() {
        boolean finish = true;
        for (Integer row : idRouterMap.keySet()) {
            for (int i = 0; i < idRouterMap.get(row).size(); i++) {
                finish = finish && idRouterMap.get(row).get(i).isEmpty();
            }
        }

        return finish;
    }


}
