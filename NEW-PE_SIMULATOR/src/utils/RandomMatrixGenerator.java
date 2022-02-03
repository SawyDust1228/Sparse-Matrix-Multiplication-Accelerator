package utils;

/**
 * @author 尹硕
 */
public class RandomMatrixGenerator {

    public RandomMatrixGenerator() {

    }

    public int[][] createMatrix(int row, int column, double density) {
        int[][] matrix = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrix[i][j] = randomNumber(density);
            }
        }
        return matrix;
    }

    public int randomNumber(double density) {
        int seed;
        seed = new java.util.Random().nextInt(100);
        if (seed <= density * 100) {
            return seed / 10;
        } else {
            return -1;
        }
    }

}
