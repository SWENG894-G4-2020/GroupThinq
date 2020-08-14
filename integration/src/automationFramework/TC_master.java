package automationFramework;

import java.io.FileWriter;   
import java.io.IOException;
import org.sikuli.script.FindFailed;

public class TC_master {

	public static void main(String[] args) throws InterruptedException, FindFailed {
		boolean doNow = true;
		Object file, result;
		
		file = CreateOutput.main(args);

		doNow = false;
		//doNow = true;
		if(doNow){ // SIGN UP TESTS
			result = TC2_1.main(args);
			UpdateResultsFile.main(file, result);
			result = TC2_2.main(args);
			UpdateResultsFile.main(file, result);
			result = TC2_3.main(args);
			UpdateResultsFile.main(file, result);
			result = TC2_4.main(args);
			UpdateResultsFile.main(file, result);
			result = TC2_5.main(args);
			UpdateResultsFile.main(file, result);
			result = TC2_6.main(args);
			UpdateResultsFile.main(file, result);
			result = TC2_7.main(args);
			UpdateResultsFile.main(file, result);
			result = TC2_8.main(args);
			UpdateResultsFile.main(file, result);		
		}
			
		
		doNow = false;
	    //doNow = true;
		if(doNow){ //   LOGIN TESTS
			result = TC1_1.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_2.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_3.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_4.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_5.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_6.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_7.main(args);
			UpdateResultsFile.main(file, result);	
			result = TC1_8.main(args);
			UpdateResultsFile.main(file, result);
		    result = TC1_9.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_10.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_11.main(args);
			UpdateResultsFile.main(file, result); 
			result = TC1_12.main(args);
			UpdateResultsFile.main(file, result);
			result = TC1_13.main(args);
			UpdateResultsFile.main(file, result);
		}
		
		doNow = false;
	    //doNow = true;
		if(doNow){ //       CREATE DECISION TESTS
			result = TC4_1.main(args);
			UpdateResultsFile.main(file, result);
			result = TC4_2.main(args);
			UpdateResultsFile.main(file, result);
			result = TC4_3.main(args);
			UpdateResultsFile.main(file, result);
			result = TC4_4.main(args);
			UpdateResultsFile.main(file, result);
			result = TC4_5.main(args);
			UpdateResultsFile.main(file, result);
		}
		
		
		doNow = false;
	    //doNow = true;
		if(doNow){ // VIEW DECISION TEST
			result = TC3_1.main(args);
			UpdateResultsFile.main(file, result);
			result = TC5_1.main(args);
			UpdateResultsFile.main(file, result);
			result = TC6_1.main(args);
			UpdateResultsFile.main(file, result);
			result = TC7_1.main(args);
			UpdateResultsFile.main(file, result);
			result = TC7_2.main(args);
			UpdateResultsFile.main(file, result);
			result = TC7_2.main(args);
			UpdateResultsFile.main(file, result);
			result = TC7_3.main(args);
			UpdateResultsFile.main(file, result);
			result = TC7_4.main(args);
			UpdateResultsFile.main(file, result);
			result = TC8_1.main(args);
			UpdateResultsFile.main(file, result);
		}
		result = TC8_1.main(args);
		UpdateResultsFile.main(file, result);
		
    }
}
