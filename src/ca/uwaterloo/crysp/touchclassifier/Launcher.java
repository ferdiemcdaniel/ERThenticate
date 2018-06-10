package ca.uwaterloo.crysp.touchclassifier;

/**Launches the main
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OfflineTests ot = new OfflineTests("data.csv");
		ot.offlineFileTest();
	}

}
