package ca.uwaterloo.crysp.touchclassifier;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import extra_trees.controller.Trainer;

/**Provides support for testing on stored data
 * Can be used for offline testing
 * @author Hassan Khan (h37khan@uwaterloo.ca)
 */
public class OfflineTests {

	String fileName;

	/*Set name of the input file*/
	public OfflineTests(String fname) {
		this.fileName = fname;
	}

	/**Read all the touch points from the file*/
	public ArrayList<TouchPoint> readFile()	{
		ArrayList <TouchPoint> touchPoints = new ArrayList<TouchPoint>();
		try {
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			int phoneID = 2, userID = 34,count = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if(line.startsWith("MD"))
					continue;
				else {
					String t[] = line.split(",");	/*Tokenize*/
					if(Integer.parseInt(t[0]) == phoneID && Integer.parseInt(t[1]) == userID){
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


	/**Execute offline tests on 'fileName'*/
	public void offlineFileTest(){
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
//				System.out.println("Time to extract features: " + (t2-t1));
//				fv[i].dumpSwipeFeatures();
			}
		}
		System.out.println("Read " + fv.length + " swipes");

		TouchClassifier tc = new TouchClassifier();

		Trainer trainer = Trainer.getInstance();


//		System.out.println("init negtrain " + tc.dataset.initializeNegTrainingSet());
		int minTrainSize = tc.dataset.getMinTrainingSetSize();
		int i = 0, j = 0;
		for (; i < minTrainSize; i++)
			tc.dataset.addToTrainingSet(fv[i].f);
		for (; j < tc.dataset.getMinTestingSetSize(); i++, j++)
			tc.dataset.addToTestingSet(fv[i].f);
		long t1 = System.nanoTime();

//		trainer.setTrainingParameters(10, 3, 2);
//		trainer.startTraining(tc.dataset.trainingSet, tc.dataset.negTrainingSet, 1, "extra/classifier", "first");

		Experiment.getInstance().startExperiment(tc.dataset.trainingSet, tc.dataset.negTrainingSet, tc.dataset.testSet, tc.dataset.negTestSet, 10, 3, 2, 25);

//		System.out.println(tc.svmWrapper.svmTrain(tc.dataset.negTrainingSet, tc.dataset.trainingSet));
		long t2 = System.nanoTime();
		System.out.println("Time to Train: " + (t2-t1));
//		for (; i < fv.length; i++) {
//			long tx = System.nanoTime();
//			tc.svmWrapper.svmEvaluate(fv[i].f);
//			long ty = System.nanoTime();
//			System.out.println("Time to Classify: " + (ty-tx));
//		}
	}
}
