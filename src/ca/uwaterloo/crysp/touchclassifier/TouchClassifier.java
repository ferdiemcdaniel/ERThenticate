package ca.uwaterloo.crysp.touchclassifier;

import java.util.ArrayList;

/**Classifies a Touch as User's or otherwise
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class TouchClassifier {

	/**Dataset for this classifier*/
	Dataset dataset;
	/**LibSVMWrapper for this classifier*/
	LibSVMWrapper svmWrapper;
	/**Features from live swipe of users*/
	ArrayList <double[]> featureVector;
	/**Training performed?*/
	private boolean isTrained = false;
	
	/**Default Constructor for the TouchClassifier
	 * Also performs NegTraining using the dataset on file*/
	public TouchClassifier() {
		this.dataset = new Dataset();
		this.svmWrapper = new LibSVMWrapper();
		this.featureVector = new ArrayList <double []> ();
		/*Initialize the negative training set*/
		this.dataset.initializeNegTrainingSet();
	}
	
	/**Add a feature vector to the Training samples. 
	 * Always Assumes that the training class is positive
	 * Negative samples are added through text file in
	 * Dataset.initializeNegTrainingSet();
	 * To reduce array creation and copying it is assumed that 
	 * index-0 of array is set as class*/
	public int addTrainingSamples(double [] fv) {
		return dataset.addToTrainingSet(fv);
	}
	/**Add array of feature vector to the training samples. */
	public int addTrainingSamples(double [][] fv) {
		return dataset.addToTrainingSet(fv);
	}
	
	/**Train the classifier*/
	public boolean train() {
		if(this.dataset.validateDataset() == false)
			return false;
		this.svmWrapper.svmTrain(this.dataset.trainingSet.toArray(new double [this.dataset.getTrainingSetSize()][]));
		this.isTrained = true;
		return true;
	}
	
	/**Evaluate a set of swipes
	 * Returns either -1/0/1 for error; someone else's swipe; user's swipe respectively*/
	public int evaluate(double [] fv) {
		if (this.isTrained == false)
			return -1;
		return (int)this.svmWrapper.svmEvaluate(fv);
	}	
}