package simulator;

import pe.PSBuffer;
import pe.PeArray;
import pe.PeColumn;

import java.util.ArrayList;
import java.util.HashMap;

public class PeArrayMap {
    private static int SIZE = 32;
    public static int PESUBARRAY_SIZE = 4;
    private HashMap<Integer, HashMap<Integer, PeArray>> peSubAarrays;
    private HashMap<Integer, Boolean> finishMap;
    private HashMap<Integer, Integer> stallCounter = new HashMap<>();
    private int counter = 1;

    public PeArrayMap() {
        peSubAarrays = new HashMap<>();
        finishMap = new HashMap<>();
        for (int i = 0; i < SIZE; i++) {
            peSubAarrays.put(i, new HashMap<>());
            for (int j = 0; j < SIZE; j++) {
                int id = getRealIndex(i, j);
                PeArray peArray = new PeArray(id, PESUBARRAY_SIZE);
                peSubAarrays.get(i).put(j, peArray);
                finishMap.put(id, false);
                stallCounter.put(id, 0);
            }
        }
    }

    private int getRealIndex(int x, int y) {
        return x * SIZE + y;
    }

    public void compute(int x, int y, int count) {
        PeArray peArray = peSubAarrays.get(x).get(y);
        peArray.compute();
        ArrayList<Boolean> stall = peArray.getNeedStall();
        int flag = 0;
        for (Boolean item : stall) {
            if (item) {
                flag = 1;
                break;
            }
        }
        stallCounter.put(getRealIndex(x, y), stallCounter.get(getRealIndex(x, y) + flag));
        if (peArray.isFinish()) {
            System.out.println("Simulator " + getRealIndex(x, y) + " finish!!!!!!!!!!");
            peArray.setFINISH(true);
            finishMap.put(getRealIndex(x, y), true);
        }
    }

    public boolean isFinish() {
        boolean finish = true;
        for (Integer key : finishMap.keySet()) {
            if (!finishMap.get(key)) {
                finish = false;
            }
        }
        return finish;
    }

}
