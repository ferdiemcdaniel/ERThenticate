package extra_trees.model;

import java.io.File;
import java.util.ArrayList;

public class Data {
	private ArrayList<double[]> inputVector;
	private ArrayList<Double> outputVector;
	private ArrayList<String> filename;
	private ArrayList<File> url_list;
	private ArrayList<Integer> classList;

	public Data(ArrayList<String> filename, ArrayList<double[]> inputVector, ArrayList<Double> outputVector, ArrayList<Integer> classList, ArrayList<File> url_list) {
		this.filename = filename;
		this.classList = classList;
		this.inputVector = inputVector;
		this.outputVector = outputVector;
		this.url_list = url_list;
	}

	public ArrayList<double[]> getInputVector() {
		return inputVector;
	}

	public ArrayList<Double> getOutputVector() {
		return outputVector;
	}

	public File getFilename(int index) {
		return url_list.get(index);
	}

	public String getFilename(String imageName) {
		File file = null;
		for (String name : filename) {
			file = null;
			file = new File(name);
			if (file.getName().equals(imageName)) {
				return name;
			}
		}

		return null;
	}

	public int getSize() {
		return filename.size();
	}

	public ArrayList<Integer> getClassList(){
		return classList;
	}

}
