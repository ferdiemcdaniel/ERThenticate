package ca.uwaterloo.crysp.touchclassifier;

/**FeatureVector class to store and process swipes
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class FeatureVector {
	/**Swipe corresponding to this feature vector*/
	Swipe swipe;
	/**configuration value for win size*/
	static int configWinSize = 5;
	/**configuration value for interswipe interval delay threshold; 10s for now*/
	static int configInterSwipeDelayThreshold = 10000;
	/**Number of features size*/
	public static final int NUM_FEAT = 29;
	/**Feature array. index 0  is left empty for class value.
	 * This should avoid recreation of arrays later
	 * And index the features as from 1 as well*/
	public double [] f = new double[NUM_FEAT];
	/**Start X (F1)*/
	/**Start Y (F2)*/
	/**End X (F3)*/
	/**End Y (F4)*/
	/**Duration in ms (F5)*/
	/**Inter-stroke time in ms (F6)*/
	/**Direct end-to-end distance (F7)*/
	/**Mean Resultant Length (F8)*/
	/**20% perc. pairwise velocity (F9)*/
	/**50% perc. pairwise velocity (F10)*/
	/**80% perc. pairwise velocity (F11)*/
	/**20% perc. pairwise acceleration (F12)*/
	/**50% perc. pairwise acceleration (F13)*/
	/**80% perc. pairwise acceleration (F14)*/
	/**Direction of End-to-End line (F15)*/
	/**Median velocity of last 3 points (F16)*/
	/**Length of Trajectory (F17)*/
    /**Average Velocity (F18)*/
    /**Median Acceleration at first 5 points (F19)*/
    /**Pressure in the middle of stroke (F20)*/
    /**Midstroke area covered (F21)*/
    /**Phone Orientation (F22)*/
    /**Direction Flag (F23)*/
    /**Ratio of Direct Distance and Traj. Length (F24)*/
    /**Average Direction of ensemble Pairs (F25)*/
    /**Largest Deviation from end-end Line (F26)*/
    /**20% perc. Deviation from end-to-end line (F27)*/
    /**50% perc. Deviation from end-to-end line (F28)*/
	/**80% perc. Deviation from end-to-end line (F29)*/

	
	/**circ_r computation. Translated from www.kyb.mpg.de/~berens/circStat.html*/
	private double circ_r(double []arr) {
		ComplexNumbers temp = new ComplexNumbers(0, 1);
		ComplexNumbers c = temp.times(arr[0]).exp();	
		for (int i = 1; i < arr.length; i++)
			c.plus(temp.times(arr[i]).exp());
		return c.abs()/arr.length;
	}

	/**circ_mean computation. Translated from www.kyb.mpg.de/~berens/circStat.html*/
	private double circ_mean(double []arr) {
		ComplexNumbers temp = new ComplexNumbers(0, 1);
		ComplexNumbers c = temp.times(arr[0]).exp();
		for (int i = 1; i < arr.length; i++)
			c.plus(temp.times(arr[i]).exp());
		return Math.atan2(c.im(), c.re());
	}

	/**Constructor for Feature Vector*/
	public  FeatureVector(Swipe swipe) {
		this.swipe = swipe;
		/*Since this is never used for negative training samples;
		 * and user will be using our provided functions for 
		 * feature extraction of positive classes, class of feature 
		 * vector is set to 1/positive class*/
	}

	/**Gets the intra-swipe features*/
	public void getIntraSwipeFeatures()
	{
		/*F 0-6 are pretty straightforward*/
		f[0] = swipe.touchpoints[0].x; /*startX*/
		f[1] = swipe.touchpoints[0].y; /*startY*/
		f[2] = swipe.touchpoints[swipe.touchpoints.length-1].x; /*endX*/
		f[3] = swipe.touchpoints[swipe.touchpoints.length-1].y; /*endY*/
		f[4] = (double)(swipe.touchpoints[swipe.touchpoints.length - 1].timestamp - 
				swipe.touchpoints[0].timestamp); /*duration*/
		f[5] = 0; /*XXX interStrokeTime: To be populated later in the pipeline*/
		f[6] = (double) Math.sqrt(Math.pow(f[3] - f[1], 2) + Math.pow(f[4] - f[2], 2)); /*directDistance*/
		
		/*Calculate pairwise displacement, velocity and acceleration*/
		double xDisplacement[] = new double[swipe.touchpoints.length - 1];
		double yDisplacement[] = new double[swipe.touchpoints.length - 1];
		double tDisplacement[] = new double[swipe.touchpoints.length - 1];
		double pairwAngle[] = new double[swipe.touchpoints.length - 1];
		double pairwDistance[] = new double[swipe.touchpoints.length - 1];
		double pairwVelocity[] = new double[swipe.touchpoints.length - 1];
		double pairwAcceleration[] = new double[swipe.touchpoints.length - 2];
		
		for (int i = 0; i < swipe.touchpoints.length - 2; i++) {
			xDisplacement[i] = swipe.touchpoints[i+1].x - swipe.touchpoints[i].x;
			yDisplacement[i] = swipe.touchpoints[i+1].y - swipe.touchpoints[i].y;
			tDisplacement[i] = swipe.touchpoints[i+1].timestamp - swipe.touchpoints[i].timestamp;
			pairwAngle[i] =  Math.atan2(yDisplacement[i], xDisplacement[i]);
			pairwDistance[i] =  Math.sqrt(Math.pow(xDisplacement[i], 2) + 
									  Math.pow(yDisplacement[i], 2));
			if (tDisplacement[i] == 0) 
				pairwVelocity[i] = 0;
			else 
				pairwVelocity[i] = pairwDistance[i]/tDisplacement[i];
		}
		/*correct pairwVelocity by setting '0' to maxVelocity*/
		double maxVelocity = ArrayUtil.max(pairwVelocity);
		for (int i = 0; i < pairwVelocity.length - 1; i++) 
			if (pairwVelocity[i] == 0)
				pairwVelocity[i] = maxVelocity;
		
		for (int i = 0; i < pairwVelocity.length - 2; i++) {
			pairwAcceleration[i] = pairwVelocity[i+1] - pairwVelocity[i];
			if (tDisplacement[i] == 0) 
				pairwAcceleration[i] = 0;
			else 
				pairwAcceleration[i] = pairwAcceleration[i]/tDisplacement[i];
		}
		/*calculate the max values for acceleration and replace
		 * values for which tDisplacement = 0 to max*/
		double maxAcceleration = 0;

		maxAcceleration = ArrayUtil.max(pairwAcceleration);
		
		for (int i = 0; i < pairwAcceleration.length - 1; i++) 
			if (pairwAcceleration[i] == 0)
				pairwAcceleration[i] = maxAcceleration;	
		
		/*F7-14*/
		f[7] = circ_r(pairwAngle); /*meanResultantLength*/
		f[8] = ArrayUtil.percentile(pairwVelocity, 0.20); /*velocity20*/
		f[9] = ArrayUtil.percentile(pairwVelocity, 0.50); /*velocity50*/
		f[10] = ArrayUtil.percentile(pairwVelocity, 0.80); /*velocity80*/
		f[11] = ArrayUtil.percentile(pairwAcceleration, 0.20); /*acceleration20*/
		f[12] = ArrayUtil.percentile(pairwAcceleration, 0.50); /*acceleration50*/
		f[13] = ArrayUtil.percentile(pairwAcceleration, 0.80); /*acceleration80*/
		f[14] = Math.atan2(f[4]-f[2], f[3]-f[1]); /*lineDirection*/
		
		/*F15 last 3 velocity points*/
		double velocityPoints [] = {pairwVelocity[pairwVelocity.length - 1],
				pairwVelocity[pairwVelocity.length-2],
				pairwVelocity[pairwVelocity.length-3]};
		 f[15] = ArrayUtil.percentile(velocityPoints, 0.50); /*medVelocity*/
		
		/*F16-17: trajectoryLength & averageVelocity*/
		 f[16] = 0;
		for (int i = 0; i < pairwDistance.length; i++) {
			 f[16] += pairwDistance[i]; /*trajectoryLength*/
		}
		if(f[4] == 0)
			f[17] = 0;
		else
			f[17] = f[16]/f[4];
		
		/*F18 - First 5 acceleration points; medianAcceleration*/
		double accelerationPoints [] = {pairwAcceleration[0],
				pairwAcceleration[1], pairwAcceleration[2],
				pairwAcceleration[3],pairwAcceleration[4],
				pairwAcceleration[5]};
		f[18] = ArrayUtil.percentile(accelerationPoints, 0.50);
		
		/*F19-21: midPressure, midArea, phoneOrientation*/
		f[19] = swipe.touchpoints[swipe.touchpoints.length/2].pressure;
		f[20] = swipe.touchpoints[swipe.touchpoints.length/2].width;
		f[21] = swipe.touchpoints[swipe.touchpoints.length/2].orientation.ordinal();
		
		/*F22 - Direction Flag. up, down, left, right are 0,1,2,3*/
		f[22] = 1;
		double xDiff = f[2] - f[0];
		double yDiff = f[3] - f[1];
		if (Math.abs(xDiff) > Math.abs(yDiff))
			if (xDiff < 0)
				f[22] = 2; //left
			else
				f[22] = 3; //right
		else
			if (yDiff < 0)
				f[23] = 0; //up
		
		/*F23-24: distToTrajRatio; averageDirection*/
		if (f[16] == 0)
			f[23] = 0;
		else
			f[23] = f[6]/f[16];
		
		f[24] = circ_mean(pairwAngle);
		
		/*F25-28 - Largest/20%/50%/80% deviation from end-to-end line*/
		double xVek [] = new double[swipe.touchpoints.length];
		double yVek [] = new double[swipe.touchpoints.length];
		for (int i = 0; i < swipe.touchpoints.length - 1; i++) {
			xVek[i] = swipe.touchpoints[i].x - f[1];
			yVek[i] = swipe.touchpoints[i].y - f[2];
		}
		double perVek[] = {yVek[yVek.length-1], xVek[xVek.length-1] - 1, 0};
		double temp = Math.sqrt(Math.pow(perVek[0], 2) + Math.pow(perVek[1], 2));
		if (temp == 0)
			perVek[0] = perVek[1] = perVek[2]  = 0;
		else {
			perVek[0] /= temp;
			perVek[1] /= temp;
			perVek[2] /= temp;
		}
		
		double absProj [] = new double[swipe.touchpoints.length];
		for (int i = 0; i < swipe.touchpoints.length - 1; i++) 
			absProj[i] = Math.abs(xVek[i] * perVek[0] + yVek[i] * perVek[1]);
		f[25] = ArrayUtil.max(absProj);
		f[26] = ArrayUtil.percentile(absProj, 0.2);
		f[27] = ArrayUtil.percentile(absProj, 0.5);
		f[28] = ArrayUtil.percentile(absProj, 0.8);
	
	}
	/**Gets the inter-swipe features*/
	public boolean getInterSwipeFeatures(FeatureVector [] featureVectors, int index)
	{
		if(featureVectors.length < index || index < configWinSize)
			return false;
		
		/*get the inter-swipe time*/
		f[5] = featureVectors[index].swipe.touchpoints[0].timestamp - 
				featureVectors[index-1].swipe.touchpoints
				[featureVectors[index-1].swipe.touchpoints.length - 1].timestamp ;
		if (f[5] < 0 || f[5] > FeatureVector.configInterSwipeDelayThreshold)
			f[5] = 0;
		
		return true;
	}
	/**Dumps features of a swipe.*/
	public void dumpSwipeFeatures() {
		for (int i = 0; i < FeatureVector.NUM_FEAT+1; i++)
			System.out.printf("%.4f ", f[i]);
		System.out.println();
		
	}

}