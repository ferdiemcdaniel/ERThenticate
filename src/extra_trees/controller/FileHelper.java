package extra_trees.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileHelper
{

	public static int countFiles(File dataFile)
	{
		BufferedReader reader;
		int count = 0;
		try {
			reader = new BufferedReader(new FileReader(dataFile));
			while (reader.readLine() != null) count++;
				reader.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		return count;
	}


	public static int countAllFiles(File file)
	{
		System.out.println("FileHerper");
		int count = 0;

		for (File classFile: file.listFiles()) {
	        if (classFile.isDirectory()) {
	        	count += classFile.list().length;
	        }
		}
		return count;
	}

}
