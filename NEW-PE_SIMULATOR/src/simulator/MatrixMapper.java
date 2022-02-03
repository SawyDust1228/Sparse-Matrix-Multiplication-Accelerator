package simulator;

import pe.PeArray;

import java.util.ArrayList;
import java.util.HashMap;

public class MatrixMapper {
    private HashMap<Integer, HashMap<Integer, PeArray>> peSubArrays;
    private int SIZE;

    public MatrixMapper(int SIZE) {
        this.SIZE = SIZE;
    }

    public void setPeSubArrays(HashMap<Integer, HashMap<Integer, PeArray>> peSubArrays) {
        this.peSubArrays = peSubArrays;
    }

    private void loadStationaryMatrix(int x, int y, int[][] stationaryData) {
        PeArray peArray = peSubArrays.get(x).get(y);
        stationaryData = transpose(stationaryData);
        peArray.setStationaryLabels(arrayToArrayList(stationaryData));
    }

    public int getLoadCycle() {
        return SIZE;
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

}
