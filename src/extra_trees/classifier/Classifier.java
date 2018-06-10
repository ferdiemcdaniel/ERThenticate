package extra_trees.classifier;


import java.io.File;
import java.util.ArrayList;

import extra_trees.controller.SolutionReader;
import extra_trees.model.Data;

public class Classifier {

	private ArrayList<Integer> classified;
	private ArrayList<ArrayList<ArrayList<Double>>> ensemble;
	private Data data;
	private int M;

	private ArrayList<ArrayList<Integer>> classificationPerTree;
	private ArrayList<Integer> treeClassification;
	private ArrayList<double[]> inputVector;
	private ArrayList<Double> outputVector;

	public Classifier()	{
		classified = new ArrayList<>();

		classificationPerTree = new ArrayList<>();
	}

	public void countTrees()	{
		M = ensemble.size();
	}

	public void individualClassifier(ArrayList<double[]> inputVector, ArrayList<Double> outputVector)	{
		this.inputVector = inputVector;
		this.outputVector = outputVector;
		countTrees();
		for(int x = 0; x < inputVector.size(); x++)	{
			double /*index*/ attr, split, classification = -1, left, right;
			int ens = 0, tree = 0;

			boolean classifying = true;

			int genuine = 0, forged = 0;

			treeClassification = new ArrayList<>();

			while(ens < M)	{
				classifying = true;

				while(classifying)	{
//					index = ensemble.get(ens).get(tree).get(0);
					attr = ensemble.get(ens).get(tree).get(1);
					split = ensemble.get(ens).get(tree).get(2);
					classification = ensemble.get(ens).get(tree).get(3);
					left = ensemble.get(ens).get(tree).get(4);
					right = ensemble.get(ens).get(tree).get(5);

					if(classification == -1)	{
						if(inputVector.get(x)[(int) attr] < split)	{
							tree = (int) left;
						}
						else	{
							tree = (int) right;
						}
					}
					else	{
						classifying = false;
						if(classification == 0)	{
							forged++;
							treeClassification.add(0);
						}
						else {
							genuine++;
							treeClassification.add(1);
						}
						System.out.println("classification = " +classification);
					}
				}
				classificationPerTree.add(treeClassification);

				ens += 1;
				tree = 0;
			}
			System.out.println("\n" +genuine+ " genuine classification");
			System.out.println(forged+ " forged classification");

			if(genuine > forged)	{
				classified.add(1);
			}
			else	{
				classified.add(0);
			}
		}
		System.out.println("claimed as: ");
		for(int i = 0; i < classified.size(); i++)	{
			System.out.print(outputVector.get(i)+ "  ");
		}
		System.out.println("\nclassified as: ");
		for(int i = 0; i < classified.size(); i++)	{
			System.out.print(classified.get(i)+ "    ");
		}
		System.out.println();
		System.out.println("Testing Accuracy = " +getTestingAccuracy());
	}

	public Double getTestingAccuracy()	{
		double accuracy;

		int misclassification = 0;

		for(int x = 0; x < classified.size(); x++)	{
			double claimed = outputVector.get(x);
			if(claimed != classified.get(x))	{
				misclassification++;
			}
		}
		int all = classified.size();

		accuracy = (((double)all - (double)misclassification)/(double)all)*100.0;

		return accuracy;
	}

	public void loadClassifier(String filename)	{
		System.out.println("loading classifier ...");
		System.out.println(filename);
		ensemble = SolutionReader.read(new File(filename));
	}

	public ArrayList<Integer> getClassified()	{
		return classified;
	}

	public Data getData()	{
		return data;
	}

}

