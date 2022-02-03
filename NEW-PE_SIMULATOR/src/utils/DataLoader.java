package utils;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 尹硕
 */

public class DataLoader {
    private String filePath;

    public DataLoader(String filePath) {
        this.filePath = filePath;
    }

    public HashMap<Integer, int[][]> readSTPUStationaryData() {
        HashMap<Integer, int[][]> result = new HashMap<>();
        MatrixReader matrixReader = new MatrixReader(filePath);
        matrixReader.setFilePathFoldName("stpu");
        for (int i = 0; i < 32 * 32; i++) {
            try {
                result.put(i, matrixReader.read("stationary" + i));
            } catch (IOException e) {
                continue;
            }
        }
        return result;
    }

    public HashMap<Integer, int[][]> readSTPUStreamingData() {
        HashMap<Integer, int[][]> result = new HashMap<>();
        MatrixReader matrixReader = new MatrixReader(filePath);
        matrixReader.setFilePathFoldName("stpu");
        for (int i = 0; i < 32; i++) {
            try {
                result.put(i, matrixReader.read("streaming" + i));
            } catch (IOException e) {
                continue;
            }
        }
        return result;
    }

    public HashMap<Integer, int[][]> readHesamStationaryData() {
        HashMap<Integer, int[][]> result = new HashMap<>();
        MatrixReader matrixReader = new MatrixReader(filePath);
        matrixReader.setFilePathFoldName("hesam");
        for (int i = 0; i < 32 * 32; i++) {
            try {
                result.put(i, matrixReader.read("stationary" + i));
            } catch (IOException e) {
                continue;
            }
        }
        return result;
    }

    public HashMap<Integer, int[][]> readHesamStreamingData() {
        HashMap<Integer, int[][]> result = new HashMap<>();
        MatrixReader matrixReader = new MatrixReader(filePath);
        matrixReader.setFilePathFoldName("hesam");
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
