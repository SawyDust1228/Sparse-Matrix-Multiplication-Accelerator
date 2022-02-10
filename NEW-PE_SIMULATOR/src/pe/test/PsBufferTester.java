package pe.test;

import pe.SubPeArray;
import pe.elements.LabelTuple;
import pe.elements.PSBuffer;
import pe.elements.PeColumn;
import utils.Answer;
import utils.DataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PsBufferTester {
    private static final int SIZE = 4;
    private static HashSet<LabelTuple> set = new HashSet<>();
    private static String PATH = "packing-algorithm/data";
    private static DataLoader dataLoader = new DataLoader(PATH, 32);
    private static HashMap<Integer, int[][]> streamingData;
    private static HashMap<Integer, HashMap<Integer, int[][]>> stationaryData;
    private static ArrayList<Integer> psMerge = new ArrayList<>();
    private static ArrayList<Integer> sameCycleMerge = new ArrayList<>();
    private static ArrayList<Integer> totalMerger = new ArrayList<>();

    public static void main(String[] args) {

        streamingData = dataLoader.readProposedStreamingData();
        stationaryData = dataLoader.readProposedStationaryData();
//        int[][] stramingData = {{2, 2, 1, 5},
//                {3, 1, 3, -1},
//                {5, 5, -1, -1},
//                {-1, -1, -1, -1}};
//        int[][] stationaryData = {{1, 18, -1, 23},
//                {1, -1, -1, 22},
//                {1, 18, -1, 23},
//                {1, 18, 19, 22}};
        for (Integer row : streamingData.keySet()) {
            for (Integer column : stationaryData.get(row).keySet()) {
                SubPeArray subPeArray = new SubPeArray(0, 4);
                run(subPeArray, stationaryData.get(row).get(column), streamingData.get(row));
            }
        }

        System.out.println("Ps buffer: " + psMerge);
        int sum = 0;
        for (int i = 0; i < psMerge.size(); i++) {
            sum += psMerge.get(i);
        }
        System.out.println("Ps Buffer total is: " + sum);
        sum = 0;
        for (int i = 0; i < sameCycleMerge.size(); i++) {
            sum += sameCycleMerge.get(i);
        }
        System.out.println("SameCycleMerge: " + sameCycleMerge);
        System.out.println("Same Cycle merge is total is: " + sum);
        sum = 0;
        for (int i = 0; i < sameCycleMerge.size(); i++) {
            sum += totalMerger.get(i);
        }
        System.out.println("Total Merge: " + totalMerger);
        System.out.println("Total Merge sum is: " + sum);
    }


    private static void run(SubPeArray subPeArray, int[][] stationaryData, int[][] stramingData) {
        Answer answer = new Answer();
        answer.addLabels(stationaryData, stramingData);
        stationaryData = transpose(stationaryData);
        subPeArray.setStationaryLabels(arrayToArrayList(stationaryData));
        subPeArray.setSteamingLabels(arrayToArrayList(stramingData));

        for (int count = 1; count <= 200; count++) {
            if (subPeArray.isFinish() && !subPeArray.isFINISH()) {
                subPeArray.setFINISH(true);
                System.out.println("Simulation finish!!!!!!!!!!");
                System.out.println("********************************************");
            }
            subPeArray.compute();

        }

        psMerge.add(subPeArray.getPsBufferMerge());
        sameCycleMerge.add(subPeArray.getSameCycleMerge());
        totalMerger.add(answer.getCount());
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
