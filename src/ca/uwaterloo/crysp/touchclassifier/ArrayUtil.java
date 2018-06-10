package ca.uwaterloo.crysp.touchclassifier;

import java.util.Arrays;

/**Useful Array statistic functions (max, variance, percentile,...)
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class ArrayUtil {

	/**Returns the maximum value in Array*/
	public static double max(double [] array) {
		double max = 0;
		for(int i = 0; i < array.length; i++)
			if (array[i] > max) 
				max = array[i];
		return max;
	}
	
	/**Returns Variance among values in array*/
	public static double variance(double [] array) {
		 double sum = 0, mean = 0, var = 0;
	     for(double d : array)
	    	 sum += d;
	     mean = sum/array.length;
	     double temp = 0;
         for(double d :array)
             temp += (mean-d)*(mean-d);
         var = temp/array.length;
         return Math.sqrt(var);
	}
	
	/**Returns the 'percentile' value in array*/
	public static double percentile(double [] array, double percentile) {
		Arrays.sort(array);
		if (array.length == 0)
			return 0;
		double k = (array.length -1 ) * percentile;
		double f = Math.floor(k);
		double c = Math.ceil(k);
		if (f == c)
			return array[(int)k];
		return array[(int)f]* (c-k) + array[(int)c] * (k-f);
	}
	
	public static double std_dev(double [] array) {
		double sum = 0;
		for (double d: array) 
			sum += d;
		
		double avg = sum/array.length;
		sum = 0;
		for (double d: array)
			sum += Math.pow(Math.abs(d-avg), 2);
		return Math.sqrt(sum/array.length);
	}
	
}
