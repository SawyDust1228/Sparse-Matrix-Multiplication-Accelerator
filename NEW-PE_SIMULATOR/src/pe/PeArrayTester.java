package pe;

import utils.Answer;
import pe.elements.LabelTuple;
import pe.elements.PSBuffer;
import pe.elements.PeColumn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PeArrayTester {
    private static final int SIZE = 4;
    private static SubPeArray subPeArray = new SubPeArray(0, 4);
    private static Answer answer = new Answer();
    private static HashSet<LabelTuple> set = new HashSet<>();

    public static void main(String[] args) {
        int[][] stramingData = {{2, 2, 1, 5},
                {3, 1, 3, -1},
                {5, 5, -1, -1},
                {-1, -1, -1, -1}};
        int[][] stationaryData = {{1, 18, -1, 23},
                {1, -1, -1, 22},
                {1, 18, -1, 23},
                {1, 18, 19, 22}};

        answer.addLabels(stationaryData, stramingData);

        stationaryData = transpose(stationaryData);
        subPeArray.setStationaryLabels(arrayToArrayList(stationaryData));
        subPeArray.setSteamingLabels(arrayToArrayList(stramingData));

        for (int count = 1; count <= 20; count++) {
            System.out.println("we are in cycle " + count);
            System.out.println("********************************************");
            if (subPeArray.isFinish() && !subPeArray.isFINISH()) {
                subPeArray.setFINISH(true);
                System.out.println("Simulation finish!!!!!!!!!!");
                System.out.println("********************************************");
//                break;
            }
            subPeArray.compute();
            System.out.println("Stall: " + subPeArray.getNeedStall());
            HashMap<Integer, PeColumn> peColumnHashMap = subPeArray.getPeColumnHashMap();
            HashMap<Integer, PSBuffer> psBufferHashMap = subPeArray.getPsBufferHashMap();
            for (int i = 0; i < SIZE; i++) {
                System.out.println("same cyle fifo " + i + " is " + peColumnHashMap.get(i).getSameCyeleMerger().getFifo());
            }
            for (int i = 0; i < SIZE; i++) {
                System.out.println("PS fifo " + i + " is " + psBufferHashMap.get(i).getFifo());
            }

            for (int i = 0; i < SIZE; i++) {
                System.out.println("PS buffer " + i + " is " + psBufferHashMap.get(i).getBuffer());
            }
            System.out.println("total fifo is " + subPeArray.getFifo());
            System.out.println("Is finish ? " + ((subPeArray.isFINISH())? "Yes!!!!!!!!!!!!" : "No"));
            System.out.println("********************************************");
        }

        System.out.println("answer is " + answer.getAnswer());
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
