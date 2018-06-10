package extra_trees.classifier;


import java.util.ArrayList;
import java.util.Random;

import extra_trees.model.Node;


public class Extra_Trees {
	private int M;
	private int nMin;
	private int K;

	private ArrayList<Node> ensemble;

	public Extra_Trees(int M, int K, int nMin) {
		this.M = M;
		this.nMin = nMin;
		this.K = K;
	}

	public int randomizeKValue() {
		Random generator = new Random();
		int K = generator.nextInt(29);
		return K;
	}

	public void buildAnExtraTreeEnsemble(ArrayList<double[]> inputvector, ArrayList<Double> outputVector) {

		ensemble = new ArrayList<>();
		for (int index = 0; index < M; index++) {
			ensemble.add(buildAnExtraTree(inputvector, outputVector));
		}
		
		System.out.println("ensemble size = " + ensemble.size());

		//Test checking the content of each ensemble
		for (int index = 0; index < ensemble.size(); index++) {
			 checkEnsemble(ensemble.get(index));
			 tempClassifier(ensemble.get(index));
		}

	}
	
	public Node buildAnExtraTree(ArrayList<double[]> inputVector,
			ArrayList<Double> outputVector) {
		Random generator = new Random();
		ArrayList<Integer> KAttrVector;
		ArrayList<Double> KSplits;
		ArrayList<Double> KSplitVarianceReductions;

		if (stopSplit(outputVector)) {
			Node node = new Node(null, null, 0, 0, inputVector, outputVector);

			int genuine = 0, forged = 0;
			for(int x = 0; x < outputVector.size(); x++)	{
				if(outputVector.get(x) == 1.0)
					genuine++;
				else forged++;
			}
			if(genuine > forged)
				node.setClassification(1.0);
			else node.setClassification(0.0);

			return node;
		} else {
			KAttrVector = new ArrayList<Integer>();
			boolean addToKVector;

			while (KAttrVector.size() != K) {
				addToKVector = true;
				int attr = generator.nextInt(28);
				for (int index = 0; index < KAttrVector.size(); index++) {
					if (KAttrVector.get(index) == attr) {
						addToKVector = false;
						break;
					}
				}
				if (addToKVector) {
					KAttrVector.add(attr);
				}
			}


			// Step2
			KSplits = new ArrayList<Double>();
			for (int s = 0; s < KAttrVector.size(); s++) {
				KSplits.add(pickARandomSplit(inputVector, KAttrVector.get(s)));
			}

			// Step3
			KSplitVarianceReductions = new ArrayList<Double>();
			for (int s = 0; s < KSplits.size(); s++) {
				double tempVarianceReduction = calculateVarianceReduction(
						inputVector, outputVector, KSplits.get(s),
						KAttrVector.get(s));
				KSplitVarianceReductions.add(tempVarianceReduction);

			}

			double maxSplitVarianceReduction = KSplitVarianceReductions.get(0);
			int maxAttr = 0;
			for (int index = 0; index < KSplitVarianceReductions.size(); index++) {
				if (maxSplitVarianceReduction < KSplitVarianceReductions
						.get(index)) {
					maxSplitVarianceReduction = KSplitVarianceReductions
							.get(index);
					maxAttr = index;
				}
			}

			// step4 split data
			ArrayList<double[]> left = new ArrayList<double[]>();
			ArrayList<double[]> right = new ArrayList<double[]>();
			ArrayList<Double> leftClass = new ArrayList<Double>();
			ArrayList<Double> rightClass = new ArrayList<Double>();

			for (int index = 0; index < inputVector.size(); index++) {
				double[] tempInput = inputVector.get(index);
				double tempClass = outputVector.get(index);
				if (tempInput[KAttrVector.get(maxAttr)] < KSplits.get(maxAttr)) {
					left.add(tempInput);
					leftClass.add(tempClass);
				} else {
					right.add(tempInput);
					rightClass.add(tempClass);
				}
			}
			
			// Step5
			Node leftSubtree = buildAnExtraTree(left, leftClass);
			Node rightSubtree = buildAnExtraTree(right, rightClass);

			// Step6
			Node parent = new Node(leftSubtree, rightSubtree,
					KAttrVector.get(maxAttr), KSplits.get(maxAttr),
					inputVector, outputVector);

			return parent;
		}
	}

	public double calculateVarianceReduction(ArrayList<double[]> inputVector,
			ArrayList<Double> outputVector, double split, int attr) {
		ArrayList<double[]> overallVector = new ArrayList<>();
		ArrayList<double[]> leftSplitVector = new ArrayList<>();
		ArrayList<double[]> rightSplitVector = new ArrayList<>();

		ArrayList<Double> classOverallVector = new ArrayList<>();
		ArrayList<Double> classLeftVector = new ArrayList<>();
		ArrayList<Double> classRightVector = new ArrayList<>();

		double overallVariance;
		double leftSplitVariance;
		double rightSplitVariance;

		double varianceReduction = 0;

		for (int index = 0; index < inputVector.size(); index++) {
			double[] temp = inputVector.get(index);
			double classTemp = outputVector.get(index);
			overallVector.add(temp);// convert inputVector from type double[][]
									// to Vector<double[]>
			classOverallVector.add(classTemp);// convert outputVector from type
												// double[] to Vector<Double>
		}

		for (int index = 0; index < inputVector.size(); index++) { // split data
																	// into left
																	// and right
																	// vectors
			double[] temp = inputVector.get(index);
			double tempClass = outputVector.get(index);
			if (temp[attr] < split) {
				leftSplitVector.add(temp);
				classLeftVector.add(tempClass);
			} else {
				rightSplitVector.add(temp);
				classRightVector.add(tempClass);
			}
		}

		double leftSplitSize = classOverallVector.size();
		double rightSplitSize = classLeftVector.size();
		double overallSize = classRightVector.size();

		// calculation of maximum variance reduction
		overallVariance = calculateVariance(classOverallVector);
		leftSplitVariance = calculateVariance(classLeftVector);
		rightSplitVariance = calculateVariance(classRightVector);

		varianceReduction += overallVariance;
		varianceReduction -= (leftSplitSize / overallSize) * leftSplitVariance;
		varianceReduction -= (rightSplitSize / overallSize)
				* rightSplitVariance;
		varianceReduction /= overallVariance;

		return varianceReduction;
	}

	public double calculateVariance(ArrayList<Double> outputVector) {
		double classValueSum = 0;
		double mean;
		double observedMinusMeanSquaredSum = 0;
		double variance;

		// calculation of the mean
		for (int index = 0; index < outputVector.size(); index++) {
			classValueSum += outputVector.get(index);
		}
		mean = classValueSum / outputVector.size();

		// calculation of the variance
		for (int index = 0; index < outputVector.size(); index++) {
			observedMinusMeanSquaredSum += Math.pow(
					(outputVector.get(index) - mean), 2);
		}
		variance = observedMinusMeanSquaredSum / (outputVector.size());

		return variance;
	}

	public double pickARandomSplit(ArrayList<double[]> inputVector, int attr) {
		Random generator = new Random();
		double rangeMin = inputVector.get(0)[attr];
		double rangeMax = inputVector.get(0)[attr];
		double split;

		for (int index = 0; index < inputVector.size(); index++) {
			if (rangeMax < inputVector.get(index)[attr]) {
				double temp = inputVector.get(index)[attr];
				rangeMax = temp;
				;
			}
			if (rangeMin > inputVector.get(index)[attr]) {
				double temp = inputVector.get(index)[attr];
				rangeMin = temp;
			}
		}

		split = rangeMin + (rangeMax - rangeMin) * generator.nextDouble();


		return split;
	}

	public boolean stopSplit(ArrayList<Double> outputVector) {
		 if(nMin >= outputVector.size()) {
			 return true;
		 }
		return false;
	}

	public ArrayList<Node> getEnsemble()	{
		return ensemble;
	}
	
	public void tempClassifier(Node node) {
		ArrayList<Node> nodeList = new ArrayList<>();
		int index = 0;
	
		nodeList.add(node);
	
		while (index < nodeList.size()) {
			if (nodeList.get(index).getLeftSubtree() != null) {
				nodeList.add(nodeList.get(index).getLeftSubtree());
				nodeList.add(nodeList.get(index).getRightSubtree());
	
				int i = nodeList.size();
				nodeList.get(index).setLeftSubtreeIndex(i - 2);
				nodeList.get(index).setRightSubtreeIndex(i - 1);
			}
			index++;
		}
	
		System.out.println("------------------------------------------------------------------------------nodeList number of nodes = " + nodeList.size());
	
		for (int x = 0; x < nodeList.size(); x++) {
			System.out.print("index = " + x);
			System.out.print("  attrindex = "
					+ nodeList.get(x).getAttributeIndex());
			System.out.print("  split = " + nodeList.get(x).getSplit());
			System.out.print("  classification = "
					+ nodeList.get(x).getClassification());
			System.out.print("  leftSubtreeIndex = "
					+ nodeList.get(x).getLeftSubtreeIndex());
			System.out.print("  rightSubtreeIndex = "
					+ nodeList.get(x).getRightSubtreeIndex());
			System.out.println();
		}
		System.out.println();
	}
	
	
	public void checkEnsemble(Node node) {
		if (node != null) {
			int genuine = 0, forged = 0;
			System.out.println("total = " + node.getInputVector().size());
			for (int index = 0; index < node.getInputVector().size(); index++) {
				if (node.getOutputVector().get(index) == 1) {
					genuine++;
				} else {
					forged++;
				}
			}
			System.out.println("genuine = " + genuine + " ---- forged = " + forged);
	
			System.out.println("left");
			checkEnsemble(node.getLeftSubtree());
			System.out.println("right");
			checkEnsemble(node.getRightSubtree());
		} else {
			System.out.println("null");
		}
	}
}
