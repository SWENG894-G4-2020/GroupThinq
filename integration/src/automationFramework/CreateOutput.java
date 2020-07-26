package automationFramework;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateOutput {
  public static  File main(String[] args) {
    try {
    	String datefileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm'_testResults.txt'").format(new Date());
    	File fileName = new File(datefileName);
      if (fileName.createNewFile()) {
        System.out.println("File created: " + fileName.getName());
        return  fileName;       
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
	return null;
    
    
  }
}