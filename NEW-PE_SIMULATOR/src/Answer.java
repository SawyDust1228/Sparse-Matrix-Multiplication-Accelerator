
import pe.LabelTuple;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * @author 尹硕
 */

public class Answer {

    private TreeSet<LabelTuple> answer = new TreeSet<>();

    public Answer() {

    }

    public TreeSet<LabelTuple> getAnswer() {
        return answer;
    }

    public void addLabels(int[][] stationaryLabel, int[][] streamingLabel) {
        ArrayList<ArrayList<Integer>> stationary = arrayToArrayList(transpose(stationaryLabel));
        ArrayList<ArrayList<Integer>> streaming = arrayToArrayList(streamingLabel);
        for (ArrayList<Integer> stationaryVector : stationary) {
            for (ArrayList<Integer> streamingVector : streaming) {
                answer.addAll(compute(stationaryVector, streamingVector));
            }
        }

    }

    private ArrayList<LabelTuple> compute(ArrayList<Integer> stationaryVector, ArrayList<Integer> streamingVector) {
        ArrayList<LabelTuple> result = new ArrayList<>();
        assert stationaryVector.size() == streamingVector.size();
        int size = stationaryVector.size();
        for (int i = 0; i < size; i++) {
            LabelTuple labelTuple = new LabelTuple(stationaryVector.get(i), streamingVector.get(i));
            if (labelTuple.isValid()) {
                result.add(labelTuple);
            } else {
                //
            }
        }
        return result;
    }

    private ArrayList<ArrayList<Integer>> arrayToArrayList(int[][] array) {
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

    private int[][] transpose(int[][] array) {
        int[][] result = new int[array[0].length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                result[j][i] = array[i][j];
            }
        }
        return result;

    }


}
