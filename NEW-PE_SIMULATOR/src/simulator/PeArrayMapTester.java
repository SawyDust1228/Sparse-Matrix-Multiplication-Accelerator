package simulator;

/**
 * @author Sawyer
 */
public class PeArrayMapTester {

    public static void main(String[] args) throws Exception {
        PeArrayMap peArrayMap = new PeArrayMap();
        peArrayMap.readAndMap();
        for (int cycle = 0; cycle < 50; cycle++) {
            if (peArrayMap.isFinish()) {
                System.out.println("Simulation finish!!!!!!!!!!");
                break;
            }
            peArrayMap.compute();
        }
    }
}
