package ca.uwaterloo.crysp.touchclassifier;


/**Stores Fields for a single touch point on the screen
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class Swipe {
	/**Raw touch data for this swipe */
	TouchPoint [] touchpoints;
	/**Number of touchpoints in this swipe*/
	int numPoints;
	/**configuration value for min swipe length*/
	static int configMinSwipeLen = 8;

	
	/**Constructor for Swipe. Takes Array of TouchPoint of a swipe*/
	public Swipe(TouchPoint [] touchpoints) {
		this.touchpoints = new TouchPoint[touchpoints.length];
		this.numPoints = touchpoints.length;
		for (int i = 0; i < numPoints; i++) 
			this.touchpoints[i] = new TouchPoint(touchpoints[i]);
	}
	
	public boolean isValidSwipe(){
		/*
		 * Discard swipes with less than 8 points
		 * No bounds checking beyond this point
		 * reducing this value WILL result in IndexOutpfBoundsException
		 * */
		if(numPoints < configMinSwipeLen)
			return false;
		return true;
	}

}
