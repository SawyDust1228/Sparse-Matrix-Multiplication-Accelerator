package pe;

import pe.elements.LabelTuple;
import pe.elements.LimitedQueue;
import pe.elements.PSBuffer;
import pe.elements.PeColumn;

import java.util.*;

/**
 * @author 尹硕
 */
public class SubPeArray {
    private int id;
    private int size; // size * size
    private HashMap<Integer, PeColumn> peColumnHashMap = new HashMap<>();
    private HashMap<Integer, PSBuffer> psBufferHashMap = new HashMap<>();
    private int[][] drainToWhere;
    private Queue<LabelTuple> fifo;
    private LimitedQueue<ArrayList<Integer>> stream;
    private Queue<ArrayList<Integer>> streamingData = new LinkedList<>();
    private Boolean[] needStall; // which column need stall
    private boolean stallFlag = false; // do we need to stall the PE
    private boolean FINISH = false;
    private ArrayList<ArrayList<Integer>> stationaryDataList = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> streamingDataList = new ArrayList<>();
//    private boolean NONE = false; // ji duan qing kaung, wu Label chan sheng;

    public SubPeArray(int id, int size) {
        assert size >= 2;
        this.id = id;
        this.size = size;
        build();
        stream = new LimitedQueue<>(size, new ArrayList<>(Collections.nCopies(size, -1)));
        fifo = new LinkedList<>();
        needStall = new Boolean[size];
        Arrays.fill(needStall, false);
    }

    public void setSteamingLabels(ArrayList<ArrayList<Integer>> data) {
        this.streamingDataList.addAll(data);
        for (ArrayList<Integer> line : data) {
            streamingData.add(line);
        }
        streamInStreamingData();
    }

    private void streamInStreamingData() {
        if (stallFlag) {
            streamStall();
            return;
        }
        ArrayList<Integer> d;
        if (!streamingData.isEmpty()) {
            d = streamingData.poll();
        } else {
            d = new ArrayList<>(Collections.nCopies(size, -1));
        }
        stream.add(d);
//        System.out.println(stream);
    }

    private void streamStall() {
        int index = -1;
        ArrayList<Integer> d = new ArrayList<>(Collections.nCopies(size, -1));
        for (int i = 0; i < size; i++) {
            if (!needStall[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            index = size;
        }
        stream.insert(index, d);
    }

    public void setStationaryLabels(ArrayList<ArrayList<Integer>> stationaryLabels) {
        assert stationaryLabels.size() == size;
        this.stationaryDataList.addAll(stationaryLabels);
        for (int i = 0; i < size; i++) {
            peColumnHashMap.get(i).setLabels(stationaryLabels.get(i));
            peColumnHashMap.get(i).findConnection();
        }
        renewDrainToWhere(stationaryLabels);
        for (int i = 0; i < size; i++) {
            peColumnHashMap.get(i).setDrainId(drainToWhere[i]);
        }
    }

    private void renewDrainToWhere(ArrayList<ArrayList<Integer>> stationaryLabels) {
        drainToWhere = new int[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(drainToWhere[i], -1);
        }
        for (int i = 0; i < size; i++) {
            HashMap<Integer, HashSet<Integer>> labelMap = peColumnHashMap.get(i).getLabelMap();
            for (int j = 0; j < size; j++) {
                Integer label = stationaryLabels.get(i).get(j);
                if (drainToWhere[i][j] == -1) {
                    if (labelMap.get(label).size() != 1) {
                        drainToWhere[i][j] = i;
                    } else if (i + 1 < size) {
                        HashMap<Integer, HashSet<Integer>> nextLabelMap = peColumnHashMap.get(i + 1).getLabelMap();
                        if (nextLabelMap.containsKey(label)) {
                            drainToWhere[i][j] = i;
                            for (Integer index : nextLabelMap.get(label)) {
                                drainToWhere[i + 1][index] = i;
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<LabelTuple> drainData() throws Exception {
        ArrayList<LabelTuple> result = new ArrayList<>();
        if (!fifo.isEmpty()) {
            result.add(fifo.poll());
        }
        LabelTuple labelTuple = null;
        for (Integer key : psBufferHashMap.keySet()) {
            if (psBufferHashMap.get(key).isFull()) {
                labelTuple = psBufferHashMap.get(key).drainBufferData();
            } else {
                if (psBufferHashMap.get(key).getFifo().isEmpty() && peColumnHashMap.get(key).getSameCyeleMerger().isEmpty() && finishStream()) {
                    if (!psBufferHashMap.get(key).getBuffer().isEmpty()) {
                        labelTuple = psBufferHashMap.get(key).drainBufferData();
                    }
                }
            }
            if (labelTuple != null) {
                result.add(labelTuple);
            }
        }
        return result;
    }

    public void compute() {
        if (FINISH) {
            return;
        }

        for (Integer key : psBufferHashMap.keySet()) {
            psBufferHashMap.get(key).addTime();
        }

        stallFlag = false;
        Arrays.fill(needStall, false);
        for (int i = 0; i < size; i++) {
            psBufferHashMap.get(i).compute();
        }
        Queue<LabelTuple> temp = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            LabelTuple labelTuple = peColumnHashMap.get(i).drainData();
            if (labelTuple != null) {
                temp.add(labelTuple);
            }
        }
        while (!temp.isEmpty()) {
            LabelTuple labelTuple = temp.poll();
            int index = labelTuple.getDrainID();
            if (index == -1) {
                fifo.add(labelTuple);
            } else {
                psBufferHashMap.get(index).addLabelTuple(labelTuple);
            }

        }
        for (int i = 0; i < size; i++) {
            boolean stall = peColumnHashMap.get(i).compute(stream.get(i));
            needStall[i] = stall;
        }
        stallFlag = renewStall();
        streamInStreamingData();
    }

    private boolean renewStall() {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (needStall[i]) {
                index = i;
            }
        }
        if (index != -1) {
            for (int i = 0; i <= index; i++) {
                needStall[i] = true;
            }
        }
        return index != -1;
    }

    private boolean finishStream() {
        return isEmpty() && streamingData.isEmpty();
    }

    private boolean isEmpty() {
        boolean result = true;
        for (int i = 0; i < size; i++) {
            ArrayList<Integer> line = stream.get(i);
            for (Integer item : line) {
                result = result && (item == -1);
            }
        }
        return result;
    }

    public boolean isFinish() {
        boolean result = true;
        for (int i = 0; i < size; i++) {
            result = result && peColumnHashMap.get(i).isEmpty();
            result = result && psBufferHashMap.get(i).isFifoEmpty();
        }
        boolean flag = false;
        for (int i = 0; i < size; i++) {
            flag = flag || !psBufferHashMap.get(i).isBufferEmpty();
        }
        boolean drainFlag = !getFifo().isEmpty();

//        return (result && !flag && !drainFlag && finishStream()) || (result && (flag || drainFlag) && finishStream());
        return (result && !flag && !drainFlag && finishStream());
    }

    private void build() {
        for (int i = 0; i < size; i++) {
            peColumnHashMap.put(i, new PeColumn(i, size));
            psBufferHashMap.put(i, new PSBuffer(i, size));
        }
    }

    public HashMap<Integer, PeColumn> getPeColumnHashMap() {
        return peColumnHashMap;
    }

    public HashMap<Integer, PSBuffer> getPsBufferHashMap() {
        return psBufferHashMap;
    }

    public void printPeStationaryLabels() {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(new ArrayList<>());
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result.get(i).add(peColumnHashMap.get(i).getColumn().get(j).getLabel());
            }
        }

        System.out.println(result);
    }

    public Queue<LabelTuple> getFifo() {
        return fifo;
    }

    public ArrayList<Boolean> getNeedStall() {
        ArrayList<Boolean> result = new ArrayList<>(Arrays.asList(needStall).subList(0, size));
        return result;
    }

    public boolean isFINISH() {
        return FINISH;
    }

    public void setFINISH(boolean FINISH) {
        this.FINISH = FINISH;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<ArrayList<Integer>> getStationaryDataList() {
        return stationaryDataList;
    }

    public ArrayList<ArrayList<Integer>> getStreamingDataList() {
        return streamingDataList;
    }

    public int getSameCycleMerge() {
        int result = 0;
        for (Integer key : psBufferHashMap.keySet()) {
//            result += psBufferHashMap.get(key).getCount();
            result += peColumnHashMap.get(key).getSameCyeleMerger().getCount();
        }
        return result;
    }

    public int getPsBufferMerge() {
        int result = 0;
        for (Integer key : psBufferHashMap.keySet()) {
            result += psBufferHashMap.get(key).getCount();
        }
        return result;
    }
}
