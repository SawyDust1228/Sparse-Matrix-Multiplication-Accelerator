package utils;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 尹硕
 */

public class DataLoader {
    private String filePath;
    private int SIZE;

    public DataLoader(String filePath, int SIZE) {
        this.filePath = filePath;
        this.SIZE = SIZE;
    }

    public HashMap<Integer, HashMap<Integer, int[][]>> readSTPUStationaryData() {
        HashMap<Integer, HashMap<Integer, int[][]>> result = new HashMap<>();
        MatrixReader matrixReader = new MatrixReader(filePath);
        matrixReader.setFilePathFoldName("stpu/stationary/");
        for (int i = 0; i < SIZE; i++) {
            HashMap<Integer, int[][]> level = new HashMap<>();
            String s = "level" + i;
            for (int j = 0; j < SIZE; j++) {
                try {
                    level.put(j, matrixReader.read(s + "/stationary" + j));
                } catch (IOException e) {
                    continue;
                }
            }
            if (level.size() != 0) {
                result.put(i, level);
            }
        }
        return result;
    }

    public HashMap<Integer, int[][]> readSTPUStreamingData() {
        HashMap<Integer, int[][]> result = new HashMap<>();
        MatrixReader matrixReader = new MatrixReader(filePath);
        matrixReader.setFilePathFoldName("stpu/streaming/");
        for (int i = 0; i < SIZE; i++) {
            try {
                result.put(i, matrixReader.read("streaming" + i));
            } catch (IOException e) {
                continue;
            }
        }
        return result;
    }

    public HashMap<Integer, HashMap<Integer, int[][]>> readProposedStationaryData() {
        HashMap<Integer, HashMap<Integer, int[][]>> result = new HashMap<>();
        MatrixReader matrixReader = new MatrixReader(filePath);
        matrixReader.setFilePathFoldName("propose/stationary/");
        for (int i = 0; i < SIZE; i++) {
            HashMap<Integer, int[][]> level = new HashMap<>();
            String s = "level" + i;
            for (int j = 0; j < SIZE; j++) {
                try {
                    level.put(j, matrixReader.read(s + "/stationary" + j));
                } catch (IOException e) {
                    continue;
                }
            }
            if (level.size() != 0) {
                result.put(i, level);
            }
        }
        return result;
    }

    public HashMap<Integer, int[][]> readProposedStreamingData() {
        HashMap<Integer, int[][]> result = new HashMap<>();
        MatrixReader matrixReader = new MatrixReader(filePath);
        matrixReader.setFilePathFoldName("propose/streaming/");
        for (int i = 0; i < 32; i++) {
            try {
                result.put(i, matrixReader.read("streaming" + i));
            } catch (IOException e) {
                continue;
            }
        }
        return result;
    }


}
