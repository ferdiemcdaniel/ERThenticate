package extra_trees.controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import extra_trees.model.Data;
import extra_trees.classifier.Extra_Trees;

public class Trainer {
	public static Trainer instance;

	private Extra_Trees extra;
	private ArrayList<double[]> inputData;
	private ArrayList<Double> outputData;

	private int M, K, nMin;

	public static Trainer getInstance(){
		if(instance == null)
			instance = new Trainer();
		return instance;
	}

	public Trainer() {

	}

	public void startTraining(final ArrayList <double[]> genuine, final ArrayList <double[]> forged, final int hiddenNodes, final String savePath, final String name){
//		new Thread(new Runnable() {
//			@Override
//			public void run() {

				loadTrainingData(genuine, forged);

				train();
				saveClassifier(savePath, createFileName(name));
				updateUserList(savePath);
//			}
//		}).start();
	}

	private void train()	{
		//printing the content of the input and output vector
//		for(int x = 0; x < inputData.size(); x++)	{
//			for(int y = 0; y < 30; y++)	{
//				System.out.print(inputData.get(x)[y]+ "  ");
//			}
//			System.out.println();
//		}
		System.out.println();
		//printing the content of the input and output vector

		extra = new Extra_Trees(M, K, nMin);
		extra.buildAnExtraTreeEnsemble(inputData, outputData);
		//Test
	}

	private void loadTrainingData(ArrayList<double[]> genuine, ArrayList<double[]> forged){
		inputData = new ArrayList<double []>(genuine);
		outputData = new ArrayList<>();
		inputData.addAll(forged);
		int i = 0, size = inputData.size();
		
		while(i< size){
			if(i < genuine.size())
				outputData.add(1.0);
			else
				outputData.add(0.0);
			i++;
		}
	}

	public void setTrainingParameters(int M, int K, int nMin)	{
		this.M = M;
		this.K = K;
		this.nMin = nMin;
	}

	public String createFileName(String name){
		String filename = name.trim().replace(" ", "_");
		return filename;
	}

	private void saveClassifier(String filepath, String filename) {
		filepath = filepath.concat("/");
		try {
			File dir = new File(filepath);
			File file = new File(filepath + filename + ".zulu");
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

	public void updateUserList(String path){
		File dir = new File(path);
		dir.mkdirs();
		File[] files_list = dir.listFiles();
	    for (int i = 0; i < files_list.length; i++) {
		      if (files_list[i].isFile()) {
		    	  String str = getOwnerName(files_list[i].getName());
		      }
	    }
	}

	private String getOwnerName(String filename){
		String[] str = filename.split(".zulu");
		String name = str[0].trim().replace("_", " ");
		return name;
	}
}
