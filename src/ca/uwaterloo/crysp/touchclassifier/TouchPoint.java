package ca.uwaterloo.crysp.touchclassifier;

/**Stores Fields for a single touch point on the screen
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class TouchPoint {

	/**Orientation of screen Portrait or landscape*/
	public enum Orientation {
		PORTRAIT, LANDSCAPE
	}
	/**Timestamp down to millisec*/
	long timestamp;
	/**Default swipeID that Android assigns to this swipe*/
	int swipeId;
	/**Horizontal position relative to top left of screen*/
	double x;
	/**Vertical position relative to top left of screen*/
	double y;
	/**Pressure as returned by Android API*/
	double pressure;
	/**Width returned by Android API*/
	double width;
	/**Orientation for this touch point*/
	Orientation orientation;
	/**Pitch of the device*/
	// double orientationPitch;
	/**Roll of the device*/
	// double orientationRoll;
	/**Azimuth of the device*/
	// double orientationAzimuth;
	/**Acceleration along x-axis of the device*/
	// double accelerometerX;
	/**Acceleration along y-axis of the device*/
	// double accelerometerY;
	/**Acceleration along z-axis of the device*/
	// double accelerometerZ;

	/**Constructor for TouchPoint*/
	public TouchPoint(long timestamp, int swipeId, double x, double y,
			double pressure, double width, Orientation orientation)	{
		this.timestamp = timestamp;
		this.swipeId = swipeId;
		this.x = x;
		this.y = y;
		this.pressure = pressure;
		this.width = width;
		this.orientation = orientation;
//		this.orientationPitch = orientationPitch;
//		this.orientationRoll = orientationRoll;
//		this.orientationAzimuth = orientationAzimuth;
//		this.accelerometerX = accelerometerX;
//		this.accelerometerY = accelerometerY;
//		this.accelerometerZ = accelerometerZ;
	}

	/**Copy constructor*/
	public TouchPoint(TouchPoint touchPoint) {
		this.timestamp = touchPoint.timestamp;
		this.swipeId = touchPoint.swipeId;
		this.x = touchPoint.x;
		this.y = touchPoint.y;
		this.pressure = touchPoint.pressure;
		this.width = touchPoint.width;
		this.orientation = touchPoint.orientation;
//		this.orientationPitch = touchPoint.orientationPitch;
//		this.orientationRoll = touchPoint.orientationRoll;
//		this.orientationAzimuth = touchPoint.orientationAzimuth;
//		this.accelerometerX = touchPoint.accelerometerX;
//		this.accelerometerY = touchPoint.accelerometerY;
//		this.accelerometerZ = touchPoint.accelerometerZ;
	}
	/**Dumps data of touchpoint*/
	public void dumpTouchPoint() {
		System.out.printf("%d;%d;%f,%f;%f;%f;%s;%f;%f;%f;%f;%f;%f",
				timestamp, swipeId, x, y, pressure, width, orientation.name());
//				orientationPitch, orientationRoll, orientationAzimuth,
//				accelerometerX,	accelerometerY, accelerometerZ);
	}
}
