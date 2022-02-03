package utils.test;


import utils.MatrixReader;

import java.io.IOException;

public class ReaderTester {

    public static void main(String[] args) throws IOException {
        MatrixReader reader = new MatrixReader("packing-algorithm/data/stpu");
        reader.setFilePathFoldName("streaming");
//        /home/xindust629/Documents/java-files/Sparse-MatrixMulti-Simulator/NEW-PE_SIMULATOR/packing-algorithm/data/stpu/streaming/streaming0
        int[][] matrix = reader.read("streaming0");
        reader.printMatrix(matrix);
    }
}
