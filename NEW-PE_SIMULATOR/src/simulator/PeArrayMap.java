package simulator;

import pe.SubPeArray;
import simulator.elements.MatrixMapper;
import utils.DataLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 尹硕
 */

public class PeArrayMap {
    private static int SIZE = 32;
    private static int PESUBARRAY_SIZE = 4;
    private static int COUNTER = 1;
    private static String PATH = "packing-algorithm/data";
    private HashMap<Integer, HashMap<Integer, SubPeArray>> subPeArrays;
    private HashMap<Integer, Boolean> finishMap;
    private HashMap<Integer, Integer> stallCounter = new HashMap<>();
    private MatrixMapper matrixMapper;
    private DataLoader dataLoader;

    public PeArrayMap() {
        subPeArrays = new HashMap<>();
        finishMap = new HashMap<>();
        matrixMapper = new MatrixMapper(PESUBARRAY_SIZE, SIZE);
        dataLoader = new DataLoader(PATH, SIZE);
        for (int i = 0; i < SIZE; i++) {
            subPeArrays.put(i, new HashMap<>());
            for (int j = 0; j < SIZE; j++) {
                int id = getRealIndex(i, j);
                SubPeArray subPeArray = new SubPeArray(id, PESUBARRAY_SIZE);
                subPeArrays.get(i).put(j, subPeArray);
                finishMap.put(id, false);
                stallCounter.put(id, 0);
            }
        }
        matrixMapper.setPeSubArrays(subPeArrays);
    }

    public void readAndMap() throws Exception {
        HashMap<Integer, HashMap<Integer, int[][]>> stationaryMatrixMap = dataLoader.readSTPUStationaryData();
        HashMap<Integer, int[][]> streamingMatrixMap = dataLoader.readSTPUStreamingData();
        mapPeArrays(stationaryMatrixMap, streamingMatrixMap);
        renewFinish();
    }

    private void mapPeArrays(HashMap<Integer, HashMap<Integer, int[][]>> stationaryMatrixMap,
                             HashMap<Integer, int[][]> streamingMatrixMap) throws Exception {
        matrixMapper.setSubMatrixMap(stationaryMatrixMap, streamingMatrixMap);
        matrixMapper.mapping();
    }

    private void renewFinish() {
        int[][] idMap = matrixMapper.getIdMap();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (idMap[i][j] == -1) {
                    int id = getRealIndex(i, j);
                    finishMap.put(id, true);
                }
            }
        }
    }

    private int getRealIndex(int x, int y) {
        return x * SIZE + y;
    }

    public void compute() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                compute(i, j);
            }
        }
    }

    private void compute(int x, int y) {
        SubPeArray subPeArray = subPeArrays.get(x).get(y);
        subPeArray.compute();
        ArrayList<Boolean> stall = subPeArray.getNeedStall();
        int flag = 0;
        for (Boolean item : stall) {
            if (item) {
                flag = 1;
                break;
            }
        }
        stallCounter.put(getRealIndex(x, y), stallCounter.get(getRealIndex(x, y) + flag));
        if (subPeArray.isFinish()) {
            System.out.println("Simulator id: " + getRealIndex(x, y) + " finish!!!!!!!!!!");
            subPeArray.setFINISH(true);
            finishMap.put(getRealIndex(x, y), true);
        }
    }

    public boolean isFinish() {
        boolean finish = true;
        for (Integer key : finishMap.keySet()) {
            if (!finishMap.get(key)) {
                return false;
            }
        }
        return finish;
    }

}
