package utils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author 尹硕
 */
public class MatrixReader {
    private String filePath;

    public MatrixReader(String filePath) {
        this.filePath = filePath;
    }

    public void setFilePathFoldName(String foldName) {
        this.filePath += "\\" + foldName;
    }

    public int[][] read(String filename) throws IOException {
        String name = filePath + "\\" + filename + ".txt";
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(name);
        InputStreamReader reader = new InputStreamReader(fileInputStream);
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        while ((strTmp = buffReader.readLine()) != null) {
            String[] strings = strTmp.split(" ");
            ArrayList<Integer> integers = new ArrayList<>();
            for (String s : strings) {
                integers.add(new Integer(s));
            }
            matrix.add(integers);
        }
        int[][] result = new int[matrix.size()][matrix.get(0).size()];
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                result[i][j] = matrix.get(i).get(j);
            }
        }
        buffReader.close();
        //printMatrix(result);
        return result;
    }

    public void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }


}
