package ca.uwaterloo.crysp.touchclassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**Dataset for training and classification
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class Dataset {
	/**
	 * Minimum number of training samples that must be present for the
	 * classifier to get invoked
	 */
	private static final int minTrainingSetSize = 20;
	private static final int minTestingSetSize = 5;
	/**
	 * The positive samples training set. These are  features provided
	 * by the user from live application usage
	 */
	ArrayList <double[]> trainingSet;

	/**
	 * The positive samples test set. These are  features provided
	 * by the user from live application usage
	 */
	ArrayList <double[]> testSet;

	/**
	 * The negative samples training set. These are  features against
	 * 20 users from sample applications
	 */
	ArrayList <double []> negTrainingSet;

	/**
	 * The negative samples test set. These are  features against
	 * 20 users from sample applications
	 */
	ArrayList <double []> negTestSet;

	/**
	 * Minimum number of training samples that must be present for the
	 * classifier to get invoked
	 */
	private boolean isNegTrainingSetInitialized = false;

	/**Constructor for DataSet class*/
	public Dataset() {
		this.trainingSet = new ArrayList <double[]> ();
		this.testSet = new ArrayList <double[]> ();
		this.negTrainingSet = new ArrayList <double[]> ();
		this.negTestSet = new ArrayList <double[]> ();
	}
	/**Returns number of training samples provided by the user*/
	public int getTrainingSetSize(){
		return this.trainingSet.size();
	}

	public int getTestingSetSize(){
		return this.testSet.size();
	}

	/**Returns minimum number of training samples required by the classifier*/
	public int getMinTrainingSetSize() {
		return minTrainingSetSize;
	}

	/**Returns minimum number of testing samples required by the classifier*/
	public int getMinTestingSetSize() {
		return minTestingSetSize;
	}

	/**Add a featureVector to the trainingSet
	 * Returns the new size of the training set*/
	public int addToTrainingSet(double[] fv) {
		/*Assert the class of training sample*/
		this.trainingSet.add(fv);
		return this.trainingSet.size();
	}

	/**Add a featureVector to the testSet
	 * Returns the new size of the testing set*/
	public int addToTestingSet(double[] fv) {
		/*Assert the class of training sample*/
		this.testSet.add(fv);
		return this.testSet.size();
	}

	/**Add an array of featureVectors to the trainingSet
	 * Returns the new size of the training set*/
	public int addToTrainingSet(double [][] fv) {
		for (int i = 0; i < fv.length; i++) {
			this.trainingSet.add(fv[i]);
		}
		return this.trainingSet.size();
	}

	/**Initializes the negative samples training set by reading it from file
	 * Negative file schema is (all values are floats/doubles):
	 * Feature-1 Feature-2 ... Feature-n \n
	 * ...
	 * Feature-1 Feature-2 ... Feature-n \n
	 * */

	public void initializeNegTrainingSet() {
		/*check whether negative training data has been populated. If no, do so*/
//		int sampleCount = 0;
//
//		try {
//			File file = new File("neg_training_set.txt");
//			FileReader fileReader = new FileReader(file);
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//			String line;
//			while ((line = bufferedReader.readLine()) != null) {
//				String t[] = line.split("\t");	/*Tokenize*/
//				double features [] = new double[FeatureVector.NUM_FEAT];
//				for(int i = 0; i < t.length; i++)
//					features[i] = Double.parseDouble(t[i]);
//				this.negTrainingSet.add(features);
//				sampleCount++;
//				if (sampleCount >= Dataset.minTrainingSetSize - 1){
//					this.negTestSet.add(features);
//				}
//			}
////			for(int i = 0; i < 68; i++)
////				System.out.println(this.negTrainingSet.get(0)[i]);
//			fileReader.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		if (sampleCount == Dataset.minTrainingSetSize - 1) {
//			System.out.println("int he loop");
//			this.isNegTrainingSetInitialized = true;
//			return true;
//		}
//		System.out.println("out of the loop");
//		return false;
		
		
		int lastIndex = 0, thisIndex = 0;
		ArrayList <TouchPoint> touchPoints = this.readFile();
		ArrayList <Swipe> swipes = new ArrayList<Swipe>();
		/*Segregate and create each swipe*/
		while (thisIndex < touchPoints.size() - 1) {
			while (thisIndex < touchPoints.size() - 1 &&
				touchPoints.get(thisIndex).swipeId == touchPoints.get(lastIndex).swipeId)
				thisIndex++;
			Swipe swipe = new Swipe(touchPoints.subList(lastIndex, thisIndex).
					toArray(new TouchPoint[thisIndex - lastIndex]));

			if(swipe.isValidSwipe())
				swipes.add(swipe);
			lastIndex = thisIndex;
		}
		FeatureVector [] fv = new FeatureVector[swipes.size()];
		for (int i = 0; i < swipes.size(); i++) {
			fv[i] = new FeatureVector(swipes.get(i));
			long t1 = System.nanoTime();
			fv[i].getIntraSwipeFeatures();
			if (i > FeatureVector.configWinSize) {
				fv[i].getInterSwipeFeatures(fv, i);
				long t2 = System.nanoTime();
			}
		}
		System.out.println("Test Read " + fv.length + " swipes");
		Random generator = new Random();
		ArrayList<Integer> negtrainIndex = new ArrayList<Integer>();
		for(int attr, j = 0 ;j<100;j++){
			attr = generator.nextInt(fv.length);
			while(negtrainIndex.contains(attr))
				attr = generator.nextInt(fv.length);
			negtrainIndex.add(attr);
		}
		int i = 0;
		for (; i < 20; i++)
			this.negTrainingSet.add(fv[negtrainIndex.get(i)].f);
		for (; i < 5; i++)
			this.negTestSet.add(fv[negtrainIndex.get(i)].f);
	}
	
	
	public ArrayList<TouchPoint> readFile()	{
		ArrayList <TouchPoint> touchPoints = new ArrayList<TouchPoint>();
		try {
			File file = new File("data.csv");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			int phoneID = 2, userID = 34,count = 0;
			ArrayList<Integer> index = new ArrayList<Integer>();
			while ((line = bufferedReader.readLine()) != null) {
				if(line.startsWith("MD"))
					continue;
				else {
					String t[] = line.split(",");	/*Tokenize*/
					if(Integer.parseInt(t[0]) == phoneID && (Integer.parseInt(t[1]) != userID) && index.size() < 20){
						count++;
						long timestamp =  Long.parseLong(t[3]);
						int rawOr = Integer.parseInt(t[5]);
						TouchPoint.Orientation orientation = TouchPoint.Orientation.PORTRAIT;
						if (rawOr == 2)
							orientation = TouchPoint.Orientation.LANDSCAPE;

						TouchPoint touchPoint = new TouchPoint(timestamp,
						  Integer.parseInt(t[4]), Double.parseDouble(t[6]),
						  Double.parseDouble(t[7]),Double.parseDouble(t[8]),
						  Double.parseDouble(t[9]), orientation);
						touchPoints.add(touchPoint);
						if(!(index.contains(Integer.parseInt(t[1]))))
							index.add(Integer.parseInt(t[1]));
					}
				}
			}
			fileReader.close();
			System.out.println("Touch Count: "+count);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return touchPoints;
	}
	
	/**Validates that the datasets are ready for consumption by SVM*/
	public boolean validateDataset() {
		if (this.trainingSet.size() <  Dataset.minTrainingSetSize ||
				this.isNegTrainingSetInitialized == false)
			return false;
		return true;
	}

}
