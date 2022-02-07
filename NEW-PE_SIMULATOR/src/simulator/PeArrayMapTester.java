package simulator;

import utils.Type;

/**
 * @author Sawyer
 */
public class PeArrayMapTester {

    public static void main(String[] args) throws Exception {
        PeArrayMap peArrayMap = new PeArrayMap();
        Type type = Type.PROPOSED;
        peArrayMap.readAndMap(type);
//        peArrayMap.printData(20, 20);
        for (int cycle = 0; cycle < 100; cycle++) {
            if (peArrayMap.isFinish()) {
                System.out.println("Simulation finish!!!!!!!!!!");
                break;
            }
            peArrayMap.compute();
        }
        peArrayMap.whoHasNotFinished();
    }
}
