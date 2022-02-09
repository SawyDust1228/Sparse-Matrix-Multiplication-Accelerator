package simulator;

import noc.RouterMap;
import noc.Sram;
import pe.SubPeArray;
import pe.elements.PSBuffer;
import pe.elements.PeColumn;
import simulator.elements.MatrixMapper;
import utils.DataLoader;
import utils.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author 尹硕
 */
public class PeArrayMap {
    private static int SIZE = 32;
    private static int PESUBARRAY_SIZE = 4;
    private static int WIRES = 4;
    private static int COUNTER = 1;
    private static String PATH = "packing-algorithm/data";
    private HashMap<Integer, HashMap<Integer, SubPeArray>> subPeArrays;
    private HashMap<Integer, Boolean> finishMap;
    private HashMap<Integer, Integer> stallCounter = new HashMap<>();
    private MatrixMapper matrixMapper;
    private DataLoader dataLoader;
    private int actives = 0;
    private HashSet<Integer> activeIdSet = new HashSet<>();
    private HashSet<Integer> finished = new HashSet<>();
    private RouterMap routerMap;
    private Sram sram;

    public PeArrayMap() {
        subPeArrays = new HashMap<>();
        finishMap = new HashMap<>();
        matrixMapper = new MatrixMapper(PESUBARRAY_SIZE, SIZE);
        dataLoader = new DataLoader(PATH, SIZE);
        sram = new Sram();
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
        routerMap = new RouterMap(SIZE, PESUBARRAY_SIZE, WIRES, subPeArrays, sram);
    }

    public void readAndMap(Type type) throws Exception {
        HashMap<Integer, HashMap<Integer, int[][]>> stationaryMatrixMap;
        HashMap<Integer, int[][]> streamingMatrixMap;
        if (type == Type.STPU) {
            stationaryMatrixMap = dataLoader.readSTPUStationaryData();
            streamingMatrixMap = dataLoader.readSTPUStreamingData();
        } else {
            stationaryMatrixMap = dataLoader.readProposedStationaryData();
            streamingMatrixMap = dataLoader.readProposedStreamingData();
        }
        mapPeArrays(stationaryMatrixMap, streamingMatrixMap);
        renewFinish();
    }

    private void mapPeArrays(HashMap<Integer, HashMap<Integer, int[][]>> stationaryMatrixMap,
                             HashMap<Integer, int[][]> streamingMatrixMap) throws Exception {
        matrixMapper.setSubMatrixMap(stationaryMatrixMap, streamingMatrixMap);
        matrixMapper.mapping();
    }

    @Deprecated
    public void printData(int x, int y) {
        SubPeArray subPeArray = subPeArrays.get(x).get(y);
        System.out.println("The Stationary Data: ");
        ArrayList<ArrayList<Integer>> sta = subPeArray.getStationaryDataList();
        for (int i = 0; i < sta.size(); i++) {
            System.out.println(sta.get(i));
        }
        System.out.println("The Streaming Data: ");
        ArrayList<ArrayList<Integer>> str = subPeArray.getStreamingDataList();
        for (int i = 0; i < str.size(); i++) {
            System.out.println(str.get(i));
        }
    }

    private void renewFinish() {
        int[][] idMap = matrixMapper.getIdMap();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (idMap[i][j] == -1) {
                    int id = getRealIndex(i, j);
                    finishMap.put(id, true);
                } else {
                    activeIdSet.add(getRealIndex(i, j));
                    actives++;
                }
            }
        }
    }

    private int getRealIndex(int x, int y) {
        return x * SIZE + y;
    }


    public void run() throws Exception {
        routerMap.horizontalMovement();
        compute();
    }

    public void printLog() {
        System.out.println("SRAM size: " + sram.getSet().size());
        System.out.println("We have merged " + sram.getCount() + " data in SRAM");
        System.out.println("Stall log: " + stallCounter);
    }

    private void compute() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                compute(i, j);
            }
        }
    }

    public void compute(int x, int y) {
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
        if (subPeArray.isFinish() && !subPeArray.isFINISH()) {
            int id = getRealIndex(x, y);
            System.out.println("Simulator id: " + id + " finish!!!!!!!!!!");
            finished.add(id);
            subPeArray.setFINISH(true);
            finishMap.put(id, true);
        }
//       debug!!!!!
//        printSubArrayLog(subPeArray);
    }

    @Deprecated
    public void whoHasNotFinished() {
        HashSet<Integer> result = new HashSet<>();
        for (Integer key : activeIdSet) {
            if (!finished.contains(key)) {
                result.add(key);
            }
        }
        if (result.size() == 0) {
            System.out.println("we don't have anything left");
            return;
        }
        System.out.println("Shit, we still have ids not finished " + result);
    }

    @Deprecated
    private void printSubArrayLog(SubPeArray subPeArray) {
//        System.out.println();
        System.out.println("*******************************************");
        System.out.println("Stall: " + subPeArray.getNeedStall());
        HashMap<Integer, PeColumn> peColumnHashMap = subPeArray.getPeColumnHashMap();
        HashMap<Integer, PSBuffer> psBufferHashMap = subPeArray.getPsBufferHashMap();
        for (int i = 0; i < PESUBARRAY_SIZE; i++) {
            System.out.println("same cyle fifo " + i + " is " + peColumnHashMap.get(i).getSameCyeleMerger().getFifo());
        }
        for (int i = 0; i < PESUBARRAY_SIZE; i++) {
            System.out.println("PS fifo " + i + " is " + psBufferHashMap.get(i).getFifo());
        }

        for (int i = 0; i < PESUBARRAY_SIZE; i++) {
            System.out.println("PS buffer " + i + " is " + psBufferHashMap.get(i).getBuffer());
        }
        System.out.println("total fifo is " + subPeArray.getFifo());
        System.out.println("Is finish ? " + ((subPeArray.isFINISH()) ? "Yes!!!!!!!!!!!!" : "No"));
        System.out.println("********************************************");
        System.out.println();
    }

    public boolean isFinish() {
        boolean finish = true;
        for (Integer key : finishMap.keySet()) {
            if (!finishMap.get(key)) {
                return false;
            }
        }
        finish = finish && routerMap.isFinish();
        return finish;
    }

}
