package extra_trees.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SolutionReader {

	private static int input_hidden_separator, hidden_output_separator;

	public static ArrayList<ArrayList<ArrayList<Double>>> read(File file) {
		ArrayList<ArrayList<ArrayList<Double>>> ensemble = new ArrayList<>();
		ArrayList<ArrayList<Double>> tree = new ArrayList<>();
		ArrayList<Double> node = new ArrayList<>();
		
		BufferedReader bufferedReader = null;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String str;
			
			while ((str = bufferedReader.readLine()) != null) {
				if(str.equals("-----"))	{
					ensemble.add(tree);
					tree = new ArrayList<>();		
				}
				else if(str.equals("-"))	{
					tree.add(node);
					node = new ArrayList<>();
				}
				else {
					double temp = Double.parseDouble(str);
					node.add(temp);
				}
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No file found.", "Error Message", JOptionPane.WARNING_MESSAGE);
			return null;
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Cannot convert to network weights.", "Error Message", JOptionPane.WARNING_MESSAGE);
			return null;
		} catch (IOException e) {
			return null;
		} finally {
            try {
                if (bufferedReader != null) {
                	bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
		}
		return ensemble;
	}

	public static int getInputHiddenSeparator() {
		return input_hidden_separator;
	}

	public static int getHiddenOutputSeparator() {
		return hidden_output_separator;
	}

}
