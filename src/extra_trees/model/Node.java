package extra_trees.model;

import java.util.ArrayList;

public class Node {
	private Node leftSubtree;
	private Node rightSubtree;

	private double split;
	private int attrIndex;
	
	private ArrayList<double[]> inputVector;
	private ArrayList<Double> outputVector;

	private int nodeListIndex;
	private int leftSubtreeIndex;
	private int rightSubtreeIndex;

	private double classification = -1;

	public Node(Node lefSubtree, Node rightSubtree, int attrIndex, double split, ArrayList<double[]> inputVector, ArrayList<Double> outputVector)	{
		this.leftSubtree = lefSubtree;
		this.rightSubtree = rightSubtree;
		this.attrIndex = attrIndex;
		this.split = split;
		this.inputVector = inputVector;
		this.outputVector = outputVector;
	}

	public Node getLeftSubtree()	{
		return leftSubtree;
	}

	public Node getRightSubtree()	{
		return rightSubtree;
	}

	public double getSplit()	{
		return split;
	}

	public int getAttributeIndex()	{
		return attrIndex;
	}

	public ArrayList<double[]> getInputVector()	{
		return inputVector;
	}

	public ArrayList<Double> getOutputVector()	{
		return outputVector;
	}

	public int getNodeListIndex()	{
		return nodeListIndex;
	}

	public void setNodeListIndex(int nodeListIndex)	{
		this.nodeListIndex = nodeListIndex;
	}

	public int getLeftSubtreeIndex()	{
		return leftSubtreeIndex;
	}

	public void setLeftSubtreeIndex(int leftSubtreeIndex)	{
		this.leftSubtreeIndex = leftSubtreeIndex;
	}

	public int getRightSubtreeIndex()	{
		return rightSubtreeIndex;
	}

	public void setRightSubtreeIndex(int rightSubtreeIndex)	{
		this.rightSubtreeIndex = rightSubtreeIndex;
	}

	public double getClassification()	{
		return classification;
	}

	public void setClassification(double classification)	{
		this.classification = classification;
	}
}

