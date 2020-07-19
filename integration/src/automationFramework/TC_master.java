package automationFramework;

import java.io.FileWriter;   
import java.io.IOException;
import org.sikuli.script.FindFailed;

public class TC_master {

	public static void main(String[] args) throws InterruptedException, FindFailed {
		Object file, result;
		
		file = CreateOutput.main(args);
		
		result = automationFramework.TC2_Signup.TC2_1.main(args);
		UpdateResultsFile.main(file, result);
		result = automationFramework.TC2_Signup.TC2_2.main(args);
		UpdateResultsFile.main(file, result);
		result = automationFramework.TC2_Signup.TC2_3.main(args);
		UpdateResultsFile.main(file, result);
		result = automationFramework.TC2_Signup.TC2_4.main(args);
		UpdateResultsFile.main(file, result);
		result = automationFramework.TC2_Signup.TC2_5.main(args);
		UpdateResultsFile.main(file, result);
		result = automationFramework.TC2_Signup.TC2_6.main(args);
		UpdateResultsFile.main(file, result);
		result = automationFramework.TC2_Signup.TC2_7.main(args);
		UpdateResultsFile.main(file, result);
		result = automationFramework.TC2_Signup.TC2_8.main(args);
		UpdateResultsFile.main(file, result);		
		
    }
}
