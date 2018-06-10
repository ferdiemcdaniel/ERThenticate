package extra_trees.controller;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import extra_trees.model.Node;


public class SolutionWriter{
	
	private File file;
	
	public SolutionWriter(File file){
		this.file = file;
	}
	
	public void saveFile(ArrayList<Node> solution){
		BufferedWriter bufferedWriter = null;
	    FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
			
			for (int x = 0; x < solution.size(); x++) {

				ArrayList<Node> nodeList = new ArrayList<>();
				int index = 0;

				nodeList.add(solution.get(x));

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
				
				for (int ctr = 0; ctr < nodeList.size(); ctr++) {
					bufferedWriter.write("" +ctr);
					bufferedWriter.newLine();
					bufferedWriter.write("" +nodeList.get(ctr).getAttributeIndex());
					bufferedWriter.newLine();
					bufferedWriter.write("" +nodeList.get(ctr).getSplit());
					bufferedWriter.newLine();
					bufferedWriter.write("" +nodeList.get(ctr).getClassification());
					bufferedWriter.newLine();
					bufferedWriter.write("" +nodeList.get(ctr).getLeftSubtreeIndex());
					bufferedWriter.newLine();
					bufferedWriter.write("" +nodeList.get(ctr).getRightSubtreeIndex());
					bufferedWriter.newLine();
					bufferedWriter.write("-");
					bufferedWriter.newLine();
				}
				bufferedWriter.write("-----");
				bufferedWriter.newLine();
			}
		} catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}
	
}
