package automationFramework;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class UpdateResultsFile {

	public static void main(Object file, Object result) {
		String myFile = file.toString();
		String myResult = result.toString();

		try (FileWriter myWriter = new FileWriter(myFile, true);
			BufferedWriter bw = new BufferedWriter(myWriter);
			PrintWriter out = new PrintWriter(bw))
		{
			out.println(myResult);
			//myWriter.close();
			System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}

}


