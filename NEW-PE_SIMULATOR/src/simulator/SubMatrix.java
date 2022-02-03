package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SubMatrix {
    private int id;
    private int SIZE;
    private HashMap<Integer, int[][]> streamingMatrix;
    private HashMap<Integer, HashMap<Integer, int[][]>> stationaryMatrix;
    private int row;
    private int column;

    public SubMatrix(int id, int SIZE, int[][] stationaryMatrix, int[][] streamingMatrix) {
        this.id = id;
        this.SIZE = SIZE;
        this.stationaryMatrix = divideStationaryMatrix(stationaryMatrix);
        this.streamingMatrix = divideStreamingMatrix(streamingMatrix);
    }

    private HashMap<Integer, int[][]> divideStreamingMatrix(int[][] matrix) {
        HashMap<Integer, int[][]> result = new HashMap<>();
        int n = matrix.length;
        int m = matrix[0].length;
        if (m % SIZE != 0) {
            int remain = SIZE - (m % SIZE);
            m += remain;
        }
        int[][] temp = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(temp[i], -1);
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[i][j] = matrix[i][j];
            }
        }
        for (int k = 0; k < (m / SIZE); k++) {
            int[][] sub = new int[n][SIZE];
            for (int j = k * SIZE; j < (k + 1) * SIZE; j++) {
                for (int i = 0; i < n; i++) {
                    sub[i][j] = temp[i][j];
                }
            }
            result.put(k, sub);
        }
        return result;
    }

    private HashMap<Integer, HashMap<Integer, int[][]>> divideStationaryMatrix(int[][] matrix) {
        HashMap<Integer, HashMap<Integer, int[][]>> result = new HashMap<>();
        int n = matrix.length;
        int m = matrix[0].length;
        if (n % SIZE != 0) {
            int remain = SIZE - (n % SIZE);
            n += remain;
        }
        this.row = n / SIZE;
        this.column = m / SIZE;
        int[][] temp = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(temp[i], -1);
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                temp[i][j] = matrix[i][j];
            }
        }

        for (int k = 0; k < (n / SIZE); k++) {
            int[][] sub = new int[SIZE][m];
            for (int i = k * SIZE; i < (k + 1) * SIZE; i++) {
                for (int j = 0; j < m; j++) {
                    sub[i][j] = temp[i][j];
                }
            }
            HashMap<Integer, int[][]> map = divideStreamingMatrix(sub);
            result.put(k, map);
        }
        return result;
    }

    public int getId() {
        return id;
    }

    public HashMap<Integer, int[][]> getStreamingMatrix() {
        return streamingMatrix;
    }

    public HashMap<Integer, HashMap<Integer, int[][]>> getStationaryMatrix() {
        return stationaryMatrix;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
