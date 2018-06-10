package ca.uwaterloo.crysp.touchclassifier;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import java.util.ArrayList;

/**Provides libSVM wrapper to be used for training and evaluation
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class LibSVMWrapper {

	/**Parameter to the SVM*/
	svm_parameter parameter;
	/**Learned Model SVM*/
	svm_model model = null;

	public LibSVMWrapper() {
		this.parameter = new svm_parameter();
		setSVMParameter(1, 0.5, 0.5, 1, svm_parameter.C_SVC, svm_parameter.RBF, 20000, 0.001);
	}

	public void setSVMParameter(int probability, double gamma,
			double nu, double C, int svmType, int kernelType,
			double cacheSize, double eps)
	{
		this.parameter.probability = probability;
		this.parameter.gamma = gamma;
		this.parameter.nu = nu;
		this.parameter.C = C;
		this.parameter.svm_type = svmType;
		this.parameter.kernel_type = kernelType;
		this.parameter.cache_size = cacheSize;
		this.parameter.eps = eps;

	}

	public boolean svmTrain(ArrayList <double []> pTrain, ArrayList <double []> nTrain) {
	    svm_problem problem = new svm_problem();
	    int dataCount = pTrain.size() + nTrain.size();
	    /*length of featureVector*/
	    problem.l = dataCount;
	    /*class of this featureVector*/
	    problem.y = new double[dataCount];
	    /*svm_nodes representing each feature vectors*/
	    problem.x = new svm_node[dataCount][];
	    int sampleCount = 0;
	    for (double features[] : pTrain) {
	    	problem.x[sampleCount] = new svm_node[features.length-1];
	        for (int j = 1; j < features.length; j++){
	            svm_node node = new svm_node();
	            node.index = j;
	            node.value = features[j];
	            problem.x[sampleCount][j-1] = node;
	        }
	        problem.y[sampleCount] = features[0];
	        sampleCount++;
	    }

	    for (double features[] : nTrain) {
	    	problem.x[sampleCount] = new svm_node[features.length-1];
	        for (int j = 1; j < features.length; j++){
	            svm_node node = new svm_node();
	            node.index = j;
	            node.value = features[j];
	            problem.x[sampleCount][j-1] = node;
	        }
	        problem.y[sampleCount] = features[0];
	        sampleCount++;
	    }

	    this.model = svm.svm_train(problem, this.parameter);
	    if (this.model == null)
	    	return false;
	    return true;
	}

	public boolean svmTrain(double [][]train) {
	    svm_problem problem = new svm_problem();
	    int dataCount = train.length;
	    /*length of featureVector*/
	    problem.l = dataCount;
	    /*class of this featureVector*/
	    problem.y = new double[dataCount];
	    /*svm_nodes representing each feature vectors*/
	    problem.x = new svm_node[dataCount][];

	    for (int i = 0; i < dataCount - 1; i++){
	        double[] features = train[i];
	        System.out.println(i);
	        problem.x[i] = new svm_node[features.length-1];
	        for (int j = 1; j < features.length; j++){
	            svm_node node = new svm_node();
	            node.index = j;
	            node.value = features[j];
	            problem.x[i][j-1] = node;
	        }
	        problem.y[i] = features[0];
	    }

	    this.model = svm.svm_train(problem, this.parameter);
	    if (this.model == null)
	    	return false;
	    return true;
	}

	public double svmEvaluate(double[] features)
	{
		if (this.model == null)
			return 0;
	    svm_node[] nodes = new svm_node[features.length-1];
	    for (int i = 1; i < features.length; i++)
	    {
	        svm_node node = new svm_node();
	        node.index = i;
	        node.value = features[i];

	        nodes[i-1] = node;
	    }

	    int totalClasses = 2;
	    int[] labels = new int[totalClasses];
	    svm.svm_get_labels(this.model,labels);

	    double[] prob_estimates = new double[totalClasses];
	    double v = svm.svm_predict_probability(this.model, nodes, prob_estimates);

	    for (int i = 0; i < totalClasses; i++){
	        System.out.print("(" + labels[i] + ":" + prob_estimates[i] + ")");
	    }
	    System.out.println("(Actual:" + features[0] + " Prediction:" + v + ")");

	    return v;
	}

	/*
	public static void main(String args[])
	{

 		double[][] train = new double[1000][];
		double[][] test = new double[10][];

		for (int i = 0; i < train.length; i++){
		    if (i+1 > (train.length/2)){        // 50% positive
		        double[] vals = {1,0,i+i};
		        train[i] = vals;
		    } else {
		        double[] vals = {0,0,i-i-i-2}; // 50% negative
		        train[i] = vals;
		    }
		}

		svm_parameter param = svmParameter(1, 0.5, 0.5, 1, svm_parameter.C_SVC, svm_parameter.RBF, 20000, 0.001);
		svm_model model = svmTrain(train, param);
		double [] d = {0,0,-10};
		double [] d1 = {1,0,10};
		svmEvaluate(d, model);
		svmEvaluate(d1, model);

		//LibSVMTest.testSVM();
	}
	*/
}
