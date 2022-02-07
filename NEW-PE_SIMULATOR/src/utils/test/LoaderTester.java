package utils.test;

import utils.DataLoader;

import java.io.IOException;
import java.util.HashMap;

public class LoaderTester {
    public static void main(String[] args) throws IOException {

//        testStationary();
//        testStreaming();
//        testProposedStreaming();
        testProposedStationary();
    }

    private static void testProposedStationary() {
        DataLoader loader = new DataLoader("packing-algorithm/data", 32);
        HashMap<Integer, HashMap<Integer, int[][]>> result = loader.readProposedStationaryData();
        for (Integer level : result.keySet()) {
            System.out.println(level + " " + result.get(level).size());
        }
    }

    private static void testProposedStreaming() {
        DataLoader loader = new DataLoader("packing-algorithm/data", 32);
        HashMap<Integer, int[][]> result = loader.readProposedStreamingData();
        for (Integer level : result.keySet()) {
            System.out.println(level + " " + result.get(level).length);
        }
    }

    private static void testStationary() {
        DataLoader loader = new DataLoader("packing-algorithm/data", 32);
        HashMap<Integer, HashMap<Integer, int[][]>> result = loader.readSTPUStationaryData();
        for (Integer level : result.keySet()) {
            System.out.println(level + " " + result.get(level).size());
        }
    }

    private static void testStreaming() {
        DataLoader loader = new DataLoader("packing-algorithm/data", 32);
        HashMap<Integer, int[][]> result = loader.readSTPUStreamingData();
        for (Integer level : result.keySet()) {
            System.out.println(level + " " + result.get(level).length);
        }
    }
}
