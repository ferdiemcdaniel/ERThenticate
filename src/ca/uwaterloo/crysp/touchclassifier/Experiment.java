package ca.uwaterloo.crysp.touchclassifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import extra_trees.classifier.Classifier;
import extra_trees.classifier.Extra_Trees;
import extra_trees.controller.SolutionWriter;

public class Experiment {
	public static Experiment instance;
	private Extra_Trees extra;
	private Classifier classifier;
	private int K, M, nMIn;
	
	
	private String URL;
	private String savePath = "extra/experiment";
	private ArrayList<double[]> train_inputData;
	private ArrayList<Double> train_outputData;
	private ArrayList<double[]> test_inputData;
	private ArrayList<Double> test_outputData;
	
	private ArrayList<ArrayList<Double>> claimedResults = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> verifiedResults = new ArrayList<>();
	private ArrayList<Double> accResults = new ArrayList<Double>();
	
	public static Experiment getInstance(){
		if(instance == null)
			instance = new Experiment();
		return instance;
	}
	
	public Experiment() {

	}
	
	public void startExperiment(final ArrayList<double[]> trainGenuine, final ArrayList<double[]> trainForged, final ArrayList<double[]> testGenuine, final ArrayList<double[]> testForged, final int M, final int K, final int nMin, final int numOfRuns){
		this.M = M;
		this.K = K;
		this.nMIn = nMin;
//		
//		new Thread(new Runnable() {			
//			@Override
//			public void run() {		
//				int n1 = (trainGenuine.length + trainForged.length) * 13 * 100;
//				int n2 = (testGenuine.length + testForged.length) * 13 * 100;
				
				clearAll();
				loadTrainingData(trainGenuine, trainForged);
				loadTestData(testGenuine, testForged);
				
				for (int i = 0; i < numOfRuns; i++) {
					System.out.println("index check: " +i);
					try {
						train();
					} catch (Exception e) {
						train();
					}
					saveClassifier(savePath, i + 1);
					test(URL);
					saveResults(testGenuine, testForged, test_outputData, classifier.getClassified(), classifier.getTestingAccuracy());
				}
				
				//test
				calculateFinalTestingAccuracy();
				//test
//			}
//		}).start();
				System.out.println(train_inputData.get(0).length);
	}
	
	private void calculateFinalTestingAccuracy()	{
		double accuracy = 0.0;
		
		for(int x = 0; x < accResults.size(); x++)	{
			accuracy += accResults.get(x);
		}
		
		System.out.println("Overall Testing Accuracy = " +accuracy/(double)accResults.size());
	}
	
	private void train()	{
		extra = new Extra_Trees(M, K, nMIn);
		extra.buildAnExtraTreeEnsemble(train_inputData, train_outputData);
	}
	
	private void loadTrainingData(ArrayList<double[]> genuine, ArrayList<double[]> forged){
		train_inputData = new ArrayList<double []>(genuine);
		train_outputData = new ArrayList<Double>();
		train_inputData.addAll(forged);
		int i = 0, size = train_inputData.size();
		
		while(i< size){
			if(i < genuine.size())
				train_outputData.add(1.0);
			else
				train_outputData.add(0.0);
			i++;
		}
		
//		printing the content of the input and output vector
			for(int x = 0; x < train_inputData.size(); x++)	{
				for(int y = 0; y < 29; y++)	{
					System.out.print(train_inputData.get(x)[y]+ "  ");
				}
				System.out.println();
			}
			System.out.println();
//		printing the content of the input and output vector
	}
	
	private void loadTestData(ArrayList<double[]> genuine, ArrayList<double[]> forged){
		test_inputData = new ArrayList<double []>(genuine);
		test_outputData = new ArrayList<Double>();
		test_inputData.addAll(forged);
		int i = 0, size = test_inputData.size();
		
		while(i< size){
			if(i < genuine.size())
				test_outputData.add(1.0);
			else
				test_outputData.add(0.0);
			i++;
		}
	}
	
	private void saveClassifier(String filepath, int filename) {
		filepath = filepath.concat("/");
		try {
			File dir = new File(filepath);
			URL = filepath + filename + ".zuluexp";
			File file = new File(URL);
			dir.mkdirs();			
			if (!file.exists())			
				file.createNewFile();
			else
				file.delete();
		    SolutionWriter fileSaver = new SolutionWriter(file);
		    fileSaver.saveFile(extra.getEnsemble()); 
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	private void test(String filename){
		classifier = new Classifier();
		classifier.loadClassifier(filename);
		classifier.individualClassifier(test_inputData, test_outputData);
	}

	public void saveResults(
			ArrayList<double[]> genuine, 
			ArrayList<double[]> forged,
			ArrayList<Double> claimed, 
			ArrayList<Integer> verified, 
			double acc){
		claimedResults.add(claimed);
		verifiedResults.add(verified);
		accResults.add(acc);
	}
	
	public void clearAll(){
		claimedResults.clear();
		verifiedResults.clear();
		accResults.clear();
		
	}
}
