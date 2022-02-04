package simulator;

/**
 * @author Sawyer
 */
public class PeArrayMapTester {

    public static void main(String[] args) throws Exception {
        PeArrayMap peArrayMap = new PeArrayMap();
        peArrayMap.readAndMap();
//        peArrayMap.printData(0, 7);
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
