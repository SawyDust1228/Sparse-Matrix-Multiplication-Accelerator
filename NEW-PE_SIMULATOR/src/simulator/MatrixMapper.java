package simulator;

import pe.PeArray;

import java.util.*;

/**
 * @author Sawyer
 */
public class MatrixMapper {
    private static int[][] DIRECTION = {{1, 0}, {0, 1}};
    private HashMap<Integer, HashMap<Integer, PeArray>> peSubArrays;
    private HashMap<Integer, SubMatrix> subMatrixMap = new HashMap<>();
    private int PE_SIZE;
    private int SIZE;
    private int[][] idMap; // sumMatrix id and subPeArrays
    private int[][] streamIdMap;    // streaming id and subPeArrays
    private int[][] stationaryIdMap; // stationary id and subPeArrays

    public MatrixMapper(int PE_SIZE, int SIZE) {
        this.PE_SIZE = PE_SIZE;
        this.SIZE = SIZE;
        idMap = new int[SIZE][SIZE];
        streamIdMap = new int[SIZE][SIZE];
        stationaryIdMap = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(idMap[i], -1);
            Arrays.fill(streamIdMap[i], -1);
            Arrays.fill(stationaryIdMap[i], -1);
        }
    }

    public void setPeSubArrays(HashMap<Integer, HashMap<Integer, PeArray>> peSubArrays) {
        this.peSubArrays = peSubArrays;
    }

    public void setSubMatrixMap(HashMap<Integer, HashMap<Integer, int[][]>> stationaryMatrixMap,
                                HashMap<Integer, int[][]> stremingMatrixMap) {
        int id = 0;
        for (Integer level : stationaryMatrixMap.keySet()) {
            for (Integer key : stationaryMatrixMap.get(level).keySet()) {
                SubMatrix subMatrix = new SubMatrix(id, PE_SIZE,
                        stationaryMatrixMap.get(level).get(key), stremingMatrixMap.get(level));
                subMatrixMap.put(id, subMatrix);
                id += 1;
            }
        }
    }

    private void mapping() throws Exception {
        for (Integer id : subMatrixMap.keySet()) {
            Boolean flag = false;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (idMap[i][j] != -1) {
                        if (BFS(i, j, id)) {
                            mapping(i, j, id);
                            flag = true;
                        }
                    }
                }
            }
            if (!flag) {
                throw new Exception("No, we can't map all this Matrix");
            }
        }

    }

    private void mapping(int x, int y, int id) {
        SubMatrix subMatrix = subMatrixMap.get(id);
        int row = subMatrix.getRow();
        int column = subMatrix.getColumn();
        for (int i = x; i <= x + row - 1; i++) {
            for (int j = y; j <= y + column - 1; j++) {
                this.idMap[i][j] = id;
                this.streamIdMap[i][j] = i - x;
                this.stationaryIdMap[i][j] = (i - x) * row + (j - y);
                loadStationaryMatrix(x, y, subMatrix.getStationaryMatrix().get(i - x).get(j - y));
                loadStreamingMatrix(x, y, subMatrix.getStreamingMatrix().get(i - x));
            }
        }
    }

    private boolean BFS(int x, int y, int id) {
        SubMatrix subMatrix = subMatrixMap.get(id);
        int row = subMatrix.getRow();
        int column = subMatrix.getColumn();
        int[] destination = new int[]{x + row - 1, y + column - 1};
        Queue<int[]> queue = new LinkedList<>();
        Boolean[][] temp = new Boolean[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (idMap[i][j] != -1) {
                    temp[i][j] = true;
                }
            }
        }
        queue.add(new int[]{x, y});
        temp[x][y] = true;
        while (!queue.isEmpty()) {
            int[] node = queue.poll();
            if (node[0] == destination[0] && node[1] == destination[1]) {
                return true;
            }
            for (int[] dirc : DIRECTION) {
                int x_new = x + dirc[0];
                int y_new = y + dirc[1];
                if (!outOfIndex(x_new, y_new, destination)) {
                    // do nothing
                    continue;
                } else {
                    if (!outOfIndex(x_new, y_new)) {
                        return false;
                    } else {
                        if (!temp[x_new][y_new]) {
                            temp[x_new][y_new] = true;
                            queue.add(new int[]{x_new, y_new});
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean outOfIndex(int x, int y, int[] bounds) {
        return x >= 0 && x <= bounds[0] && y >= 0 && y <= bounds[1];
    }

    private boolean outOfIndex(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }


    private void loadStationaryMatrix(int x, int y, int[][] stationaryData) {
        PeArray peArray = peSubArrays.get(x).get(y);
        stationaryData = transpose(stationaryData);
        peArray.setStationaryLabels(arrayToArrayList(stationaryData));
    }


    public void loadStreamingMatrix(int x, int y, int[][] streamingData) {
        PeArray peArray = peSubArrays.get(x).get(y);
        peArray.setSteamingLabels(arrayToArrayList(streamingData));
    }


    private static ArrayList<ArrayList<Integer>> arrayToArrayList(int[][] array) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < array[i].length; j++) {
                temp.add(array[i][j]);
            }
            result.add(temp);
        }
        return result;
    }

    private static int[][] transpose(int[][] array) {
        int[][] result = new int[array[0].length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[j][i] = array[i][j];
            }
        }
        return result;
    }

    public int getLoadCycle() {
        return PE_SIZE;
    }


}
